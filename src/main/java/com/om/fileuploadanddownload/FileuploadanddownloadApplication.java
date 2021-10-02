package com.om.fileuploadanddownload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FileuploadanddownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileuploadanddownloadApplication.class, args);
	}

	// for CORS issue
	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/file").allowedOrigins("http://localhost:3000");
			}
		};

	}
}
