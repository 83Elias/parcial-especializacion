package com.eurekaserver.eurekka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekkaApplication.class, args);
	}

}
