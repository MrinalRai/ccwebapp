package com.neu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.neu.controller","com.neu.repository","com.neu.service","com.neu.config"})
public class CloudAssignment2Application extends SpringBootServletInitializer{
	
	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(CloudAssignment2Application.class);
	    }

    
	public static void main(String[] args) {
		SpringApplication.run(CloudAssignment2Application.class, args);
	}

}
