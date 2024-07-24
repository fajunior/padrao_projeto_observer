package br.com.padraoprojeto.observer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.padraoprojeto.observer.dto.ObserverServiceDTO;
import br.com.padraoprojeto.observer.dto.ServiceOrderDTO;
import br.com.padraoprojeto.observer.entities.ServiceOrder;

@Service
public class EmailService {
	@Value("${subject.to_process}")
	private String uriGetListToBeProcessed;
	
	@Value("${subject.process}")
	private String uriProcessItem;
	
	@Value("${subject.register.observer}")
	private String uriRegisterObserver;
	
	

	private int count = 0;
	
	public void process() {
		count++;
		try {
			RestTemplate restTemplate = new RestTemplate();
			
			ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new JavaTimeModule());
	        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        
			
	        ResponseEntity<String> response = restTemplate.getForEntity(uriGetListToBeProcessed, String.class);

			
	        List<ServiceOrder> serviceOrders = mapper.readValue(response.getBody(), new TypeReference<List<ServiceOrder>>() {});
			
			serviceOrders.forEach(serviceOrder -> {
				sendEmail(serviceOrder.getId());
			});

			String message = String.format("Response(%d): ", count);
			System.out.println(message);
			
			System.out.println("Email enviado! ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	public void sendEmail(int id) {
		String uri = String.format(uriProcessItem, id);
		try {
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<ObserverServiceDTO> requestEntity = new HttpEntity<>(headers);
			
			ServiceOrderDTO response = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, ServiceOrderDTO.class).getBody();
			
			if (response != null) {
				
				System.out.println("Response: " + response);
			}
			System.out.println("Email enviado! ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void register() {

		try {

			RestTemplate restTemplate = new RestTemplate();

			ObserverServiceDTO dto = new ObserverServiceDTO("http://localhost:8081/api/observer");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<ObserverServiceDTO> requestEntity = new HttpEntity<>(dto, headers);

			Boolean response = restTemplate.exchange(uriRegisterObserver, HttpMethod.POST, requestEntity, Boolean.class).getBody();

			System.out.println("Response: " + response);
			System.out.println("Observer registrado");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
