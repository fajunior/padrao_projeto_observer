package br.com.padraoprojeto.observer.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.padraoprojeto.observer.entities.ServiceOrder;
import br.com.padraoprojeto.observer.repositories.ServiceOrderRepository;

@RestController
@RequestMapping("/api/serviceorders")
public class ServiceOrderController {
	@Autowired
	private ServiceOrderRepository serviceOrderRepository;
	
	private List<String> statusList = new ArrayList<>(List.of("novo", "em andamento", "em validação", "encerrado"));
 
	@RequestMapping(method = {RequestMethod.POST})
	public ServiceOrder createServiceOrder(@RequestParam String status) {
		ServiceOrder serviceOrder = new ServiceOrder(status);
		return serviceOrderRepository.save(serviceOrder);
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
		return serviceOrderRepository.save(serviceOrder.get());
	}
	
	@GetMapping(path = "/notProcessed")
	public Iterable<ServiceOrder> notProcessed() {
		return serviceOrderRepository.findByProcessedFalse();
	}
}
