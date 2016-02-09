package com.chemicaltracker.controller;

import com.chemicaltracker.Application;
import com.chemicaltracker.model.request.*;
import com.chemicaltracker.model.response.*;
import static com.chemicaltracker.TestUtil.json;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class APIControllerTest {

	private static final String VALID_CHEMICAL = "toluene";
	private static final String INVALID_CHEMICAL = "invalidChemical";

	@Autowired
    private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void queryExistingChemical() throws Exception {

		ChemicalQueryRequest request = new ChemicalQueryRequest(VALID_CHEMICAL);
		mockMvc.perform(post("/api/query")
				.content(json(request))
				.contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.match", is(true)))
			.andExpect(jsonPath("$.chemical", is(VALID_CHEMICAL)))
			.andExpect(jsonPath("$.properties.health", is(2)))
			.andExpect(jsonPath("$.properties.flammability", is(3)))
			.andExpect(jsonPath("$.properties.instability", is(0)))
			.andExpect(jsonPath("$.properties.notice", is("H")));
	}

	@Test
	public void queryNonExistingChemical() throws Exception {

		ChemicalQueryRequest request = new ChemicalQueryRequest(INVALID_CHEMICAL);
		mockMvc.perform(post("/api/query")
					.content(json(request))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.match", is(false)));
	}
}