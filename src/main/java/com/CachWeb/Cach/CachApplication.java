package com.CachWeb.Cach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class CachApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachApplication.class, args);
	}

}
