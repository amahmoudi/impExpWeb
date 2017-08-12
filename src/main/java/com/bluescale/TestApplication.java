package com.bluescale;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.bluescale.business.configuration.AppConfig;
import com.bluescale.business.model.User;
import com.bluescale.business.service.UserService;

@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	
	// Exemple d'insertion
//	  AbstractApplicationContext context = new
//	  AnnotationConfigApplicationContext(AppConfig.class);
//	  UserService service = (UserService) context.getBean("userService");
//	  User User3 = new User();
//	  User3.setUserEmail("abdelbaki.mahmoudi@bluescale.com");
//	  User3.setUserPassword("admin"); User3.setUserEnabled(true);
//	  User3.setDateDownloadFile(new Date()); User3.setDateUploadFile(new
//	  Date()); User3.setAdminEnabled(true); service.saveUser(User3);
//	  context.close();
		 

	}
}
