package br.com.padraoprojeto.observer.services;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.padraoprojeto.observer.entities.ServiceOrder;

@Service
public class EmailService {
	@Value("${subject.to_process}")
	private String uriGetListToBeProcessed;
	
	@Value("${subject.process}")
	private String uriProcessItem;
	
	public void process() {
		int count=0;
		while (true) {		
			count++;
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				// Criando o objeto de requisição GET
				HttpGet request = new HttpGet(this.uriGetListToBeProcessed);
				
				// Executando a requisição
				try (CloseableHttpResponse response = httpClient.execute(request)) {
					// Obtendo a resposta
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						String result = EntityUtils.toString(entity);
						
						ObjectMapper mapper = new ObjectMapper();
						mapper.enable(SerializationFeature.INDENT_OUTPUT);
						mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
				        mapper.registerModule(new JavaTimeModule());
				        
						List<ServiceOrder> serviceOrders = mapper.readValue(result, 
			                    TypeFactory.defaultInstance().constructCollectionType(List.class, ServiceOrder.class));
						
						serviceOrders.forEach(serviceOrder->{
							sendEmail(serviceOrder.getId());
						});
						
						String message = String.format("Response(%d): ", count);
						System.out.println(message + result);
						
					}
				}
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void sendEmail(int id) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			String uri = String.format(this.uriProcessItem,id);
			HttpPut request = new HttpPut(uri);
			
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String result = EntityUtils.toString(entity);
					System.out.println("Response: " + result);
				}
				System.out.println("Email enviado! ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
