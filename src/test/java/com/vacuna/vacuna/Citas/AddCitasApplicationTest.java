package com.vacuna.vacuna.Citas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

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

import com.google.gson.Gson;
import com.vacuna.vacuna.VacunaApplication;
import com.vacuna.vacuna.model.CentroSanitario;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc
public class AddCitasApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext controller;
	@BeforeEach
	public void setupMockMvc(){
	    mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
	
	@Test
	public void saveCita() throws Exception {
		saveCita1();
		saveCita2();
	}
	
	public void saveCita1() throws Exception{
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		long fechaActual = new Date(tomorrow.getYear(),tomorrow.getMonth(), tomorrow.getDate(), 8, 0).getTime();
		
		//CentroSanitario centroSanitario = new CentroSanitario("hospitalillo", 0, 2, "Ciudad Real", "Ciudad Real");
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/cita/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
		
		JSONParser parser = new JSONParser(); 
		Object parsedJson = parser.parse(result.getResponse().getContentAsString());
		
		String fechaPrimeraDosis = ((JSONObject) parsedJson).getAsString("fechaPrimeraDosis");
		assertEquals(String.valueOf(fechaActual), fechaPrimeraDosis);	
		
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.put("/cita/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
		
		JSONParser parser1 = new JSONParser(); 
		Object parsedJson1 = parser.parse(result.getResponse().getContentAsString());
		
		String fechaPrimeraDosis1 = ((JSONObject) parsedJson).getAsString("fechaPrimeraDosis");
		assertEquals(String.valueOf(fechaActual), fechaPrimeraDosis1);	
	}
	
	public void saveCita2() throws Exception{
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		long fechaActual = new Date(tomorrow.getYear(),tomorrow.getMonth(), tomorrow.getDate(), 8, 0).getTime();
		//{"id":"c8724d3e-6e22-4af2-80e8-3380463a5711","fechaPrimeraDosis":1635577200000,"fechaSegundaDosis":1637395200000} 30/10 - 09:00 20/11 - 09:00
		MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.put("/cita/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
		fechaActual += 3600000; //Siguiente rango de horas
		JSONParser parser2 = new JSONParser(); 
		Object parsedJson2 = parser2.parse(result2.getResponse().getContentAsString());
				
		String fechaPrimeraDosis2 = ((JSONObject) parsedJson2).getAsString("fechaPrimeraDosis");
		assertEquals(String.valueOf(fechaActual), fechaPrimeraDosis2);	
				
		MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.put("/cita/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
				
		JSONParser parser3 = new JSONParser(); 
		Object parsedJson3 = parser2.parse(result3.getResponse().getContentAsString());
				
		String fechaPrimeraDosis3 = ((JSONObject) parsedJson3).getAsString("fechaPrimeraDosis");
		assertEquals(String.valueOf(fechaActual), fechaPrimeraDosis3);	
					
	}
}
