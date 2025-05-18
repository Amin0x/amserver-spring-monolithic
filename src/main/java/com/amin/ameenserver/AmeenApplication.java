package com.amin.ameenserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmeenApplication {

	public  static final int serverVersion = 1;
	public  static final int ApiVersion = 1;
	public  static final int ClientMinVersion = 1;

	public static void main(String[] args) {
		SpringApplication.run(AmeenApplication.class, args);
	}

//	@Bean(name = "multipartResolver")
//	public CommonsMultipartResolver multipartResolver() {
//		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//		multipartResolver.setMaxUploadSize(100000);
//		return multipartResolver;
//	}
}
