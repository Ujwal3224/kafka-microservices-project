package com.example.orders_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.orders_services", "com.example.commonlibrary"})
public class OrdersServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersServicesApplication.class, args);
	}

}
