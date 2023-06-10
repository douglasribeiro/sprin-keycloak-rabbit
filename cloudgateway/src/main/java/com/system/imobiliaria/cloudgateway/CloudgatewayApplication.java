package com.system.imobiliaria.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class CloudgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudgatewayApplication.class, args);
	}
	
	@Bean
	public RouteLocator route(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/cliente/**").uri("lb://cliente"))
				.route(r -> r.path("/cartao/**").uri("lb://cartao"))
				.route(r -> r.path("/avaliacoes-credito/**").uri("lb://avaliadorcredito"))
				.build();
	}

}
