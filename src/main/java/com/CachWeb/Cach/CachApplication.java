package com.CachWeb.Cach;

import com.CachWeb.Cach.email.EmailService;
import com.CachWeb.Cach.entity.Currency;
import com.CachWeb.Cach.entity.ExchangeRequest;
import com.CachWeb.Cach.entity.Image;
import com.CachWeb.Cach.entity.Wallet;
import com.CachWeb.Cach.service.ExchangeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;

@EnableWebMvc
@SpringBootApplication
public class CachApplication {



	public static void main(String[] args) {
		SpringApplication.run(CachApplication.class, args);
	}

}
