package com.vacuna.vacuna.Usuario;

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
public class FindAndReadUserApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext controller;
	@BeforeEach
	public void setupMockMvc(){
	    mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
	
	@Test
	public void readAll() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/paciente/getTodos").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
		//Verificación del estado 
		assertEquals(200, result.getResponse().getStatus());
	}
	
	
	@Test
	public void findPaciente() throws Exception {
		String jsonStr = "{\"dni\":\"05937392B\"}";
		JSONParser parser = new JSONParser(); 
		JSONObject parsedJson = (JSONObject) parser.parse(jsonStr);
		
		String dni =parsedJson.getAsString("dni");
		assertEquals("05937392B", dni);
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/paciente/buscarDni/05937392B").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStr.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		
		//Verificacion:
		assertEquals(200, result.getResponse().getStatus());
		
		//El usuario no existe: Fallo 1
		String jsonStrFallo1 = "{\"dni\":\"77777777H\"}";
		JSONParser parser1 = new JSONParser(); 
		JSONObject parsedJson1 = (JSONObject) parser1.parse(jsonStrFallo1);
		
		String dni1 =parsedJson1.getAsString("dni");
		assertEquals("77777777H", dni1);
		
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/paciente/buscarDni/01724987H").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo1.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		assertEquals(409, result1.getResponse().getStatus());
		
		//Introducir DNI incorrecto: prueba fallo 3
		String jsonStrFallo2 = "{\"nombre\":\"Lucas\",\"apellidos\":\"Carrtero\",\"dni\":\"66666H\",\"tipoUsuario\":\"paciente\",\"centroAsignado\":\"Hospitalillo\",\"dosisAdministradas\":\"2\",\"localidad\":\"CR\",\"provincia\":\"CR\"}";
		JSONParser parser2 = new JSONParser(); 
		JSONObject parsedJsonFallo2 = (JSONObject) parser2.parse(jsonStrFallo2);
					
		String dni2 = parsedJsonFallo2.getAsString("dni");
		assertEquals("66666H", dni2);
		MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/paciente/buscarDni/66666H").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo2.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
					
		assertEquals(409, result2.getResponse().getStatus());
	}
}
