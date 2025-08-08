package com.arcilio.henrique.ms_event_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MsEventManagerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(MsEventManagerApplication.class, args);
	}

}
