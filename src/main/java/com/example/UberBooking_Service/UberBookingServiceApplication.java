package com.example.UberBooking_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("com.example.UberProject_EntityService.models")
public class UberBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberBookingServiceApplication.class, args);
	}

}
