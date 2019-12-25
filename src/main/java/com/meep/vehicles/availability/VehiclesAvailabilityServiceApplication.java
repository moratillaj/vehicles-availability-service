package com.meep.vehicles.availability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VehiclesAvailabilityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehiclesAvailabilityServiceApplication.class, args);
	}

}
