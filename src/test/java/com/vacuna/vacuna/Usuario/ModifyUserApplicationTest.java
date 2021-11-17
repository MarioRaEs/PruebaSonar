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
import com.vacuna.vacuna.model.Paciente;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import com.google.gson.Gson; 

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc
public class ModifyUserApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext controller;
		
	@BeforeEach
	public void setupMockMvc(){
		mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
	
	@Test
	public void modificarPaciente() throws Exception {
		String jsonStr = "{\"dni\":\"71555585H\"}";
		JSONParser parser = new JSONParser(); 
		JSONObject parsedJson = (JSONObject) parser.parse(jsonStr);
		
		String dni =parsedJson.getAsString("dni");
		assertEquals("71555585H", dni);
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/paciente/buscarDni/71555585H").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStr.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		
		String content = result.getResponse().getContentAsString();
		JSONObject parsedContent = (JSONObject) parser.parse(content);
		
		String nombre = parsedContent.getAsString("nombre");
		String email = parsedContent.getAsString("email");
		byte[] passsword = "hello world".getBytes();
		String centroAsignado = parsedContent.getAsString("centroAsignado");
		String dosisAdministradas = parsedContent.getAsString("dosisAdministradas");
		String localidad = parsedContent.getAsString("localidad");
		String provincia =parsedContent.getAsString("provincia");
		
		Paciente p = new Paciente(nombre, email, passsword, dni, centroAsignado,
				dosisAdministradas, localidad, provincia, provincia);
		
		p.setNombre(nombre);
		p.setEmail("mario@gmail.com");
		p.setPassword(passsword);
		p.setCentroAsignado("Hola");
		p.setDosisAdministradas(dosisAdministradas);
		p.setLocalidad(localidad);
		p.setProvincia(provincia);
		
		Gson gson = new Gson();
		String json = gson.toJson(p);
		
		MvcResult cambios = mockMvc.perform(MockMvcRequestBuilders.put("/paciente/modificarUsuarios").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)
				.content(json.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		
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
		
		//Campos vacios: Fallo 2
		p.setNombre("");
		
		Gson gson1 = new Gson();
		String json1 = gson.toJson(p);
		
		MvcResult cambios1 = mockMvc.perform(MockMvcRequestBuilders.put("/paciente/modificarUsuarios").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)
				.content(json1.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		assertEquals(409, cambios1.getResponse().getStatus());
				
	}

}
