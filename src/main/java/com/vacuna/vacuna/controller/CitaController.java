package com.vacuna.vacuna.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vacuna.vacuna.DAO.CentroSanitarioDAO;
import com.vacuna.vacuna.DAO.citaDAO;
import com.vacuna.vacuna.DAO.pacienteDAO;
import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Cita;
import com.vacuna.vacuna.model.Paciente;
import com.vacuna.vacuna.model.Usuario;

@RestController
@RequestMapping("cita")
public class CitaController {

	CentroSanitario centroSanitario;

	@Autowired
	private citaDAO repositoryCita;

	@Autowired
	private CentroSanitarioDAO repositoryCentro;

	@Autowired
	private pacienteDAO repositoryPaciente;

	@PutMapping("/add")
	public Cita add(@RequestBody Map<String, Object> info) throws Exception {
		JSONObject jso = new JSONObject(info);
		String dniPaciente = jso.optString("dni");
		Paciente paciente = repositoryPaciente.findByDni(dniPaciente);
		System.out.println("DNI: "+paciente.getDni());
		String nombreCentro = jso.optString("centroAsignado");
		if ((Integer.parseInt(paciente.getDosisAdministradas()) < 1)
				&& (repositoryCita.findByDniPaciente(dniPaciente) == null)) {
			Cita c = crearCita(nombreCentro, dniPaciente);
			return repositoryCita.insert(c);
		} else {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Paciente con alguna dosis administrada o cita ya asignada");
		}
	}

	@GetMapping("/getTodos")
	public List<Cita> get() {
		try {
			return repositoryCita.findAll();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@Transactional
	@DeleteMapping("/eliminarCita/{id}")
	public Paciente eliminarCita(@PathVariable String id) {
		Optional<Cita> c = repositoryCita.findById(id);
		Cita citaAux = new Cita();
		if (c.isPresent()) {
			citaAux = c.get();
		}

		String nombreCentro = citaAux.getNombreCentro();
		CentroSanitario cs = repositoryCentro.findByNombre(nombreCentro);
		cs.setDosisTotales(cs.getDosisTotales() + 2);
		repositoryCentro.save(cs);
		repositoryCita.deleteById(id);
		return null;
	}

	@GetMapping("/")
	public List<Cita> readAll() {
		return repositoryCita.findAll();
	}

	@Transactional
	@PutMapping("/modificarCita/{id}")
	public Cita modificarCita(@PathVariable String id) throws Exception {
		Optional<Cita> c = repositoryCita.findById(id);
		Cita citaAux = new Cita();
		if (c.isPresent()) {
			citaAux = c.get();
		}
		Cita citaModificada = crearCita(citaAux.getNombreCentro(), citaAux.getDniPaciente());
		citaAux.setFechaPrimeraDosis(citaModificada.getFechaPrimeraDosis());
		citaAux.setFechaSegundaDosis(citaModificada.getFechaSegundaDosis());
		return repositoryCita.save(citaAux);
	}

	public Cita crearCita(String nombreCentro, String dniPaciente) throws Exception {

		List<Cita> listaCitas = repositoryCita.findAll();
		List<CentroSanitario> listaCentros = repositoryCentro.findAll();

		Cita c = new Cita();

		for (int i = 0; i < listaCentros.size(); i++) {
			if (listaCentros.get(i).getNombre().equals(nombreCentro)) {
				centroSanitario = listaCentros.get(i);
			}
		}

		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

		long fechaActual = new Date(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDate(), 8, 0).getTime();
		long nocheVieja = 1640980800000L;
		int contadorAforo = 0; // Aforo para el centro que cogemos
		boolean asignada = true;

		if (listaCitas.isEmpty()) {
			if (centroSanitario.getDosisTotales() >= 2) {
				++contadorAforo;
				c = new Cita();
				c.setNombreCentro(centroSanitario.getNombre());
				c.setFechaPrimeraDosis(fechaActual);
				c.setFechaSegundaDosis(fechaActual + (1000 * 60 * 60 * 24 * 21));
				c.setDniPaciente(dniPaciente);
				centroSanitario.setDosisTotales(centroSanitario.getDosisTotales() - 2);
				repositoryCentro.save(centroSanitario);
				return c;
			} else {
				throw new Exception("No hay dosis disponibles.");
			}
		} else {
			asignada = false;
			while (!asignada) {
				for (int i = 0; i < listaCitas.size(); i++) {
					Cita cita = listaCitas.get(i);
					if ((cita.getFechaPrimeraDosis() == fechaActual) || (cita.getFechaSegundaDosis() == fechaActual)) {
						++contadorAforo;
					}
				}
				if (contadorAforo >= centroSanitario.getAforo()) {
					if (new Date(fechaActual).getHours() == 20) {
						fechaActual += (3600000 * 12); // Proximo dia a las 08.00am
					} else {
						fechaActual += 3600000; // Siguiente rango de horas
					}
					if ((fechaActual >= nocheVieja) || (fechaActual + (1000 * 60 * 60 * 24 * 21) >= nocheVieja)) {
						throw new ResponseStatusException(HttpStatus.CONFLICT,
								"La fecha es superior o igual al 31 de Diciembre de 2021");
					}
					contadorAforo = 0;
				} else {
					++contadorAforo;
					c.setNombreCentro(centroSanitario.getNombre());
					c.setFechaPrimeraDosis(fechaActual);
					c.setFechaSegundaDosis(fechaActual + (1000 * 60 * 60 * 24 * 21));
					c.setDniPaciente(dniPaciente);
					centroSanitario.setDosisTotales(centroSanitario.getDosisTotales() - 2);
					repositoryCentro.save(centroSanitario);
					asignada = true;
					return c;
				}
			}
		}

		return c;
	}

}
