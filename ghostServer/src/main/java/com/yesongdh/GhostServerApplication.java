package com.yesongdh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yesongdh.mapper")
public class GhostServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GhostServerApplication.class, args);
	}

}
