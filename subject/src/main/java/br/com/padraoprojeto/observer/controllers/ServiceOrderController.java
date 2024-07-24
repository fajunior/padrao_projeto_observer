package br.com.padraoprojeto.observer.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.padraoprojeto.observer.dto.ObserverServiceDTO;
import br.com.padraoprojeto.observer.entities.ServiceOrder;
import br.com.padraoprojeto.observer.repositories.ServiceOrderRepository;

@RestController
@RequestMapping("/api/serviceorders")
public class ServiceOrderController {
	@Autowired
	private ServiceOrderRepository serviceOrderRepository;
	
	private List<String> statusList = new ArrayList<>(List.of("novo", "em andamento", "em validação", "encerrado"));
	private List<String> observerList = new ArrayList<String>();
 
	@RequestMapping(method = {RequestMethod.POST})
	public ServiceOrder createServiceOrder(@RequestParam String status) {
		ServiceOrder serviceOrder = new ServiceOrder(status);
		
		serviceOrder = serviceOrderRepository.save(serviceOrder);
		
		observerList.forEach(uri->{
			this.notifyObserver(uri);
		});
		return serviceOrder;
	}
	
	@RequestMapping(method = {RequestMethod.GET})
	public Iterable<ServiceOrder> list() {
		return serviceOrderRepository.findAll();
	}
	
	@GetMapping(path = "/lastMinute")
	public Iterable<ServiceOrder> listLastMinute() {
		LocalDateTime time = LocalDateTime.now().minusMinutes(1);
		return serviceOrderRepository.findByLastUpdatedBiggerDate(time);
	}
	
	@PutMapping(path = "/{id}/process")
	public ServiceOrder process(@PathVariable int id) {
		Optional<ServiceOrder> serviceOrder = serviceOrderRepository.findById(id);
		serviceOrder.get().setProcessed(true);
		return serviceOrderRepository.save(serviceOrder.get());
	}
	
	@PutMapping(path = "/{id}/changeStatus")
	public ServiceOrder changeStatus(@PathVariable int id) {
		Optional<ServiceOrder> serviceOrder = serviceOrderRepository.findById(id);
		int index = this.statusList.indexOf(serviceOrder.get().getStatus());
		serviceOrder.get().setProcessed(false);
		serviceOrder.get().setStatus(this.statusList.get(index+1));
		
		ServiceOrder saved = serviceOrderRepository.save(serviceOrder.get());
		
		observerList.forEach(uri->{
			this.notifyObserver(uri);
		});
		
		return saved;
	}
	
	@GetMapping(path = "/notProcessed")
	public Iterable<ServiceOrder> notProcessed() {
		return serviceOrderRepository.findByProcessedFalse();
	}
	
	@PostMapping(path="/register")
	public boolean registerObserver(@RequestBody ObserverServiceDTO dto) {
		this.observerList.add(dto.getUri());
		return true;
	}
	
	private void notifyObserver(String uri) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			
			HttpPost request = new HttpPost(uri);
			
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String result = EntityUtils.toString(entity);
					System.out.println("Response: " + result);
				}
				System.out.println("Notificado! ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
