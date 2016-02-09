package com.chemicaltracker.controller;

import com.chemicaltracker.Application;
import com.chemicaltracker.model.request.*;
import com.chemicaltracker.model.response.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import javax.annotation.Resource;
import org.springframework.security.web.FilterChainProxy;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AuthorizationTest {

	private static final String VALID_USERNAME = "kevin";
	private static final String VALID_PASSWORD = "pass";
	private static final String INVALID_USERNAME = "invalidUsername";
	private static final String INVALID_PASSWORD = "invalidPassword";

    @Resource
    private FilterChainProxy springSecurityFilterChain;

	@Autowired
    private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext)
						.addFilter(springSecurityFilterChain)
						.build();
	}

	@Test
	public void unauthorizedUser() throws Exception {
		String credentials = INVALID_USERNAME + ":" + INVALID_PASSWORD;  
		String base64EncodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes("utf-8"));  
		
		mockMvc.perform(post("/api/authorize")
			.header("Authorization", "Basic " + base64EncodedCredentials))
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void authorizedUser() throws Exception {
		String credentials = VALID_USERNAME + ":" + VALID_PASSWORD;  
		String base64EncodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes("utf-8"));  
		
		mockMvc.perform(post("/api/authorize")
			.header("Authorization", "Basic " + base64EncodedCredentials))
			.andExpect(status().isOk());
	}
}