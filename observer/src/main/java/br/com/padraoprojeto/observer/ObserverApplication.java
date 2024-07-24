package br.com.padraoprojeto.observer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.padraoprojeto.observer.services.EmailService;

@SpringBootApplication
public class ObserverApplication {
	@Autowired
	private EmailService emailService;	

	public static void main(String[] args) {
		SpringApplication.run(ObserverApplication.class, args);
	}
	
	@PostConstruct
    public void init() {
        emailService.register();
    }

}
