package com.pge.ecm.crmis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrmisLiteApplication {

	public static void main(String[] args) {

		SpringApplication.run(CrmisLiteApplication.class, args);
		System.out.println("Started version 1.0.0.3");
	}

}
