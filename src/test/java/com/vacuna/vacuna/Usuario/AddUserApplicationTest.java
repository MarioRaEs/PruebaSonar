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

import com.jayway.jsonpath.JsonPath;
import com.vacuna.vacuna.VacunaApplication;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc
public class AddUserApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext controller;
	@BeforeEach
	public void setupMockMvc(){
	    mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
	
	
	@Test
	public void savePaciente() throws Exception {
		String jsonStr = "{\"nombre\":\"Lucas\",\"apellidos\":\"Carretero\",\"dni\":\"86666666H\",\"tipoUsuario\":\"paciente\",\"centroAsignado\":\"Hospitalillo\",\"dosisAdministradas\":\"2\",\"localidad\":\"CR\",\"provincia\":\"CR\"}";
		JSONParser parser = new JSONParser(); 
		JSONObject parsedJson = (JSONObject) parser.parse(jsonStr);

		String nombre = parsedJson.getAsString("nombre");
		assertEquals("Lucas", nombre);
			
		String apellidos = parsedJson.getAsString("apellidos");
		assertEquals("Carretero", apellidos);
			
		String dni = parsedJson.getAsString("dni");
		assertEquals("86666666H", dni);
			
		String tipoUsuario = parsedJson.getAsString("tipoUsuario");
		assertEquals("paciente", tipoUsuario);
			
		String centroAsignado = parsedJson.getAsString("centroAsignado");
		assertEquals("Hospitalillo", centroAsignado);
			
		String dosisAdministradas = parsedJson.getAsString("dosisAdministradas");
		assertEquals("2", dosisAdministradas);
			
		String localidad = parsedJson.getAsString("localidad");
		assertEquals("CR", localidad);
			
		String provincia =parsedJson.getAsString("provincia");
		assertEquals("CR", provincia);
			
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/paciente/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStr.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
			
		//Verificacion
		assertEquals(200, result.getResponse().getStatus());
			
			
		//Nombre y dni vacios: prueba fallo 1
		String jsonStrFallo1 = "{\"nombre\":\"\",\"apellidos\":\"Carrtero\",\"dni\":\"\",\"tipoUsuario\":\"paciente\",\"centroAsignado\":\"Hospitalillo\",\"dosisAdministradas\":\"2\",\"localidad\":\"CR\",\"provincia\":\"CR\"}";
		JSONParser parser1 = new JSONParser(); 
		JSONObject parsedJsonFallo1 = (JSONObject) parser1.parse(jsonStrFallo1);
			
		String nombre1 = parsedJsonFallo1.getAsString("nombre");
		assertEquals("", nombre1);
			
		String dni1 = parsedJsonFallo1.getAsString("dni");
		assertEquals("", dni1);
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.put("/paciente/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo1.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
			
		assertEquals(409, result1.getResponse().getStatus());
			
		//Creando un usuario ya existente: prueba fallo 2
		String jsonStrFallo2 = "{\"nombre\":\"Lucas\",\"apellidos\":\"Carrtero\",\"dni\":\"66666666H\",\"tipoUsuario\":\"paciente\",\"centroAsignado\":\"Hospitalillo\",\"dosisAdministradas\":\"2\",\"localidad\":\"CR\",\"provincia\":\"CR\"}";
		JSONParser parser2 = new JSONParser(); 
		JSONObject parsedJsonFallo2 = (JSONObject) parser2.parse(jsonStrFallo2);
			
		String dni2 = parsedJsonFallo2.getAsString("dni");
		assertEquals("66666666H", dni2);
		MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.put("/paciente/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo2.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
			
		assertEquals(409, result2.getResponse().getStatus());
			
		//Introducir DNI incorrecto: prueba fallo 3
		String jsonStrFallo3 = "{\"nombre\":\"Lucas\",\"apellidos\":\"Carrtero\",\"dni\":\"66666H\",\"tipoUsuario\":\"paciente\",\"centroAsignado\":\"Hospitalillo\",\"dosisAdministradas\":\"2\",\"localidad\":\"CR\",\"provincia\":\"CR\"}";
		JSONParser parser3 = new JSONParser(); 
		JSONObject parsedJsonFallo3 = (JSONObject) parser3.parse(jsonStrFallo3);
			
		String dni3 = parsedJsonFallo3.getAsString("dni");
		assertEquals("66666H", dni3);
		MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.put("/paciente/add").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo3.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
			
		assertEquals(409, result2.getResponse().getStatus());
	}
}
