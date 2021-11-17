package com.vacuna.vacuna.Citas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

public class DeleteCitasApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext controller;
		
	@BeforeEach
	public void setupMockMvc(){
		   mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
	
	@Test
	public void deleleCita() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/cita/eliminarCita/0b316dd7-9a0e-4356-a098-b927aa7eb7b7").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
		//Prueba de verificacion:
		assertEquals(200, result.getResponse().getStatus());
		
		//La cita no existe: Fallo 1
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.delete("/cita/eliminarCita/0b316dd7-9a0e-4356-a098-b927aa7eb7b7").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
		assertEquals(409, result1.getResponse().getStatus());
	}

}
