package com.yesongdh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages= {"com.yesongdh.controller"})
public class GhostServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GhostServerApplication.class, args);
	}

}
