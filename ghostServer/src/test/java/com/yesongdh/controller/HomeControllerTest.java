package com.yesongdh.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void test() {
		
	}

}
