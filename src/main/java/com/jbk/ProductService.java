package com.jbk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component("com")
public class ProductService {

	public static void main(String[] args) {
		SpringApplication.run(ProductService.class, args);
		System.out.println("done");
	}

}
