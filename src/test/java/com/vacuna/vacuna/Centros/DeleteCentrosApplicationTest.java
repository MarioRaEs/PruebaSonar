package com.vacuna.vacuna.Centros;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.vacuna.vacuna.VacunaApplication;

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc
public class DeleteCentrosApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext controller;
	@BeforeEach
	public void setupMockMvc(){
	    mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
	
	@Test
	public void deleteCentro() throws Exception {
		String jsonStr = "{\"id\":\"6181aec6f92e751f3365fbcf\"}";
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/centro/eliminarCentro/6181aec6f92e751f3365fbcf").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStr.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		//Verificacion
		assertEquals(200, result.getResponse().getStatus());
		
		//El centro no existe: fallo 1
		String jsonStrFallo1 = "{\"id\":\"6181aec6f92e751f3365fbcf\"}";
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.delete("/centro/eliminarCentro/6181aec6f92e751f3365fbcf").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo1.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		assertEquals(409, result1.getResponse().getStatus());
				
	}
}
