package org.thoughts.on.java.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
public class OrderInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderInfoApplication.class, args);
	}

}
