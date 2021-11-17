package com.vacuna.vacuna.controller;

import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vacuna.vacuna.DAO.CentroSanitarioDAO;
import com.vacuna.vacuna.DAO.UsuarioDAO;
import com.vacuna.vacuna.model.Administrador;
import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Paciente;
import com.vacuna.vacuna.model.Sanitario;
import com.vacuna.vacuna.model.Usuario;

@RestController
@RequestMapping("Usuario")
@CrossOrigin("*")
public class UsuarioController {

	@Autowired
	private UsuarioDAO repository;

	@Autowired
	private CentroSanitarioDAO csrepository;

	@PutMapping("/add")
	public void add(@RequestBody Map<String, Object> info) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		System.out.println("hola");
		JSONObject jso = new JSONObject(info);
		String nombre = jso.optString("nombre");
		String email = jso.optString("email");
		String dni = jso.optString("dni");
		String tipoUsuario = jso.optString("tipoUsuario");
		String password = jso.optString("password");
		String centroAsignado = jso.optString("centroAsignado");
		String localidad = jso.optString("localidad");
		String provincia = jso.optString("provincia");

		byte[] pwd = md.digest(password.getBytes());
		System.out.println(pwd);
		if (nombre.isEmpty() || dni.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Debes rellenar todos los campos del usuario");
		}

		validarDNI(dni);

		if (repository.findByDni(dni) != null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
		}

		if (tipoUsuario.equals("Paciente")) {
			Paciente usuario = new Paciente(nombre, email, pwd, dni, tipoUsuario, centroAsignado, "0", localidad, provincia);
			repository.insert(usuario);
		} else if (tipoUsuario.equals("Sanitario")) {
			Sanitario usuario = new Sanitario(nombre, email, pwd, dni, tipoUsuario);
			repository.insert(usuario);
		} else {
			Administrador usuario = new Administrador(nombre, email, pwd, dni, tipoUsuario);
			repository.insert(usuario);
		}

	}

	@GetMapping("/getTodos")
	public List<Usuario> get() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@GetMapping("/getCentros")
	public List<CentroSanitario> devolverCentro() {
		try {
			return csrepository.findAll();

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}

	}

	@Transactional
	@DeleteMapping("/eliminarUsuario/{dni}")
	public Usuario eliminarUsuario(@PathVariable String dni) {
		validarDNI(dni);
		if (repository.findByDni(dni) == null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario no existe");
		}
		return repository.deleteByDni(dni);

	}

	@GetMapping("/buscarDni/{dni}")
	public Usuario readAll(@PathVariable String dni) {
		validarDNI(dni);
		if (repository.findByDni(dni) == null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario no existe");
		}
		return repository.findByDni(dni);
	}

	@RequestMapping("/modificarUsuarios")
	public Paciente update(@RequestBody Paciente p) {
		if (repository.findByDni(p.getDni()) == null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario no existe");
		}
		if (p.getCentroAsignado().isEmpty() || p.getDosisAdministradas().isEmpty() || p.getLocalidad().isEmpty()
				|| p.getNombre().isEmpty() || p.getProvincia().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Rellena todos los campos correctamente");
		}
		return repository.save(p);
	}

	public boolean validarDNI(String dni) {
		boolean valido = true;
		if (dni.length() != 9) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI no valido, cantidad de digitos incorrecta");
		}
		String entero = dni.substring(0, 8);

		if (!entero.matches("[0-9]+")) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI no valido, intentelo de nuevo");
		}
		String letra = dni.substring(8, 9);
		if (letra.matches("[0-9]+")) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI no valido, intentelo de nuevo");
		}
		return valido;
	}
}