package com.homeinventory;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeinventoryApplication {

	@Autowired
	private EntityManagerFactory emf;

	public static void main(String[] args) {
		SpringApplication.run(HomeinventoryApplication.class, args);
	}

	@PostConstruct
	public void printEntities() {
		System.out.println("📦 Entities managed by Hibernate:");
		emf.getMetamodel().getEntities().forEach(e ->
				System.out.println(" - " + e.getName()));
	}

}
