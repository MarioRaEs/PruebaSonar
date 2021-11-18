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

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc
public class AddCentrosApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext controller;
	@BeforeEach
	public void setupMockMvc(){
	    mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
	
	@Test
	private void saveCentro() throws Exception {
		String jsonStr = "{\"nombre\":\"Hospital General\",\"dosisTotales\":\"300\",\"aforo\":\"250\",\"localidad\":\"CR\",\"provincia\":\"CR\"}";
		JSONParser parser = new JSONParser(); 
		JSONObject parsedJson = (JSONObject) parser.parse(jsonStr);
	
		String nombre = parsedJson.getAsString("nombre");
		assertEquals("Hospital General", nombre);
			
		String dosisTotales = parsedJson.getAsString("dosisTotales");
		assertEquals("300", dosisTotales);
			
		String aforo = parsedJson.getAsString("aforo");
		assertEquals("250", aforo);
			
			
		String localidad = parsedJson.getAsString("localidad");
		assertEquals("CR", localidad);
			
		String provincia =parsedJson.getAsString("provincia");
		assertEquals("CR", provincia);
			
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/centro/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStr.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
			
		//Verificacion
		assertEquals(200, result.getResponse().getStatus());
		
		//Datos imcompletos = Fallo 1
		String jsonStrFallo1 = "{\"nombre\":\"\",\"dosisTotales\":\"300\",\"aforo\":\"250\",\"localidad\":\"CR\",\"provincia\":\"CR\"}";
		JSONParser parser1 = new JSONParser(); 
		JSONObject parsedJsonFallo1 = (JSONObject) parser1.parse(jsonStrFallo1);
		
		String nombre1 = parsedJsonFallo1.getAsString("nombre");
		assertEquals("", nombre1);
		
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.put("/paciente/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo1.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
			
		assertEquals(409, result1.getResponse().getStatus());
	}
}
