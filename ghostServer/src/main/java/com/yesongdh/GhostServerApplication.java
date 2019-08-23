package com.yesongdh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@MapperScan(basePackages = "com.yesongdh.mapper")
public class GhostServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GhostServerApplication.class, args);
	}

}
