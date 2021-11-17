package com.vacuna.vacuna.controller;

import java.security.MessageDigest;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vacuna.vacuna.DAO.UsuarioDAO;
import com.vacuna.vacuna.DAO.pacienteDAO;
import com.vacuna.vacuna.model.Paciente;
import com.vacuna.vacuna.model.Usuario;


@RestController
@RequestMapping("login")
public class LoginController {

	@Autowired
	private UsuarioDAO userRepository;
	
	@GetMapping("/recoverPwd")
	public void recoverPwd(@RequestParam String email) {
		try {
			//Paciente user = userRepository.findByEmail(email);
//			if (user!=null) {
//				Email smtp = new Email();
//				String texto = "Para recuperar tu contraseña, pulsa aquí: " + 
//					"http://localhost/user/usarToken/" + token.getId() + "";
//				smtp.send(email, "Carreful: recuperación de contraseña", texto);
//			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public Usuario login(HttpServletRequest request, @RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			String email = jso.optString("email");
			if (email.length()==0)
				throw new Exception("Debes indicar tu nombre de usuario");
			String password= jso.optString("password");
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] pwd = md.digest(password.getBytes());
			System.out.println("email: "+email+" password: "+pwd);
			Usuario user = userRepository.findByEmailAndPassword(email,pwd);

			if (user==null)
				throw new Exception("Credenciales inválidas");
			request.getSession().setAttribute("userEmail", email);
			return user;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		
	}
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
		try {
			request.getSession().removeAttribute("userEmail");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
}
