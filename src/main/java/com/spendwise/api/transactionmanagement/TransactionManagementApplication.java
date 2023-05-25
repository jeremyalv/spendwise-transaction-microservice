package com.spendwise.api.transactionmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class TransactionManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(TransactionManagementApplication.class, args);
	}

	@Configuration
	@EnableWebMvc
	public class CorsConfig implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/api/v1/transactions/**")
					.allowedOrigins("*")
					.allowedMethods("GET", "POST", "PUT", "DELETE");
		}
	}
}
