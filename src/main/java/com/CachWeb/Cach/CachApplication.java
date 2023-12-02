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
public class CachApplication implements CommandLineRunner {
	@Autowired
	public ExchangeRequestService exchangeRequestService;

//	@Autowired
//	private EmailService emailServic;
	public static void main(String[] args) {
		SpringApplication.run(CachApplication.class, args);
	}
//	@EventListener(ApplicationReadyEvent.class)
//	public  void sendEmail(){
//		emailServic.sendEmail("mmeezzoo212@gmail.com",
//				"تم اضافة طلب جديد","اهلا ادمن تم اضافة طلب جديد");
//	}

	@Override
	public void run(String... args) throws Exception {

//		Wallet wallet = new Wallet();
//		wallet.setWalletNumber("535353");
//		Image image = new Image();
//		Currency c1 = new Currency();
//		c1.setCode("fff");
//		c1.setName("ffffff");
//		for(int i =0;i<10;i++) {
//
//
//
//			ExchangeRequest exchangeRequest = new ExchangeRequest();
//			exchangeRequest.setImage(image);
//			exchangeRequest.setWallet(wallet);
//			exchangeRequest.setReceivingAmount(BigDecimal.valueOf(90));
//			exchangeRequest.setSendingAmount(BigDecimal.valueOf(900));
//			exchangeRequest.setSourceCurrency(c1);
//			exchangeRequest.setTargetCurrency(c1);
//			System.out.println("Save  =" +i);
//			exchangeRequestService.Save(exchangeRequest);
//		}
		System.out.println("Finished");
	}
}
