package br.com.padraoprojeto.observer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.padraoprojeto.observer.services.EmailService;

@RestController
@RequestMapping("/api/observer")
public class EmailNotificationController {
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(method = {RequestMethod.POST})
	public void process() {
		this.emailService.process();
	}
}
