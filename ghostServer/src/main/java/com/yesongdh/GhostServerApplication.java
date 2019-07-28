package com.yesongdh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
//@ComponentScan(basePackages= {"com.yesongdh.controller"})
public class GhostServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GhostServerApplication.class, args);
	}

}
