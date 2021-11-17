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


@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc

public class DeleteUserApplicationTest {
	

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext controller;
		
	@BeforeEach
	public void setupMockMvc(){
		   mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
		
	@Test
	public void delelePacienteCorrecto() throws Exception {
		String jsonStr = "{\"dni\":\"11777777H\"}";
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/paciente/eliminarUsuario/11777777H").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStr.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		//Verificacion
		assertEquals(200, result.getResponse().getStatus());
		
		//El usuario no existe: fallo 1
			String jsonStrFallo1 = "{\"dni\":\"11777777H\"}";
			MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.delete("/paciente/eliminarUsuario/11777777H").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo1.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
			assertEquals(409, result1.getResponse().getStatus());
				
		
		//DNI incorrecto: fallo 2
		String jsonStrFallo2 = "{\"dni\":\"177777H\"}";
		MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.delete("/paciente/eliminarUsuario/11777777H").contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8).content(jsonStrFallo2.getBytes())).andDo(MockMvcResultHandlers.print()).andReturn();
		assertEquals(409, result2.getResponse().getStatus());
		
		
	}
	
}
