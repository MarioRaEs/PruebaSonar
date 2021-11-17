package com.vacuna.vacuna.controller;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Optional;

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
import com.vacuna.vacuna.DAO.pacienteDAO;
import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Paciente;

@RestController
@RequestMapping("centro")
@CrossOrigin("*")
public class CentroController {

	@Autowired
	private CentroSanitarioDAO repository;
	
	@Autowired
	private pacienteDAO pacienteDao;

	@PutMapping("/add")
	public CentroSanitario add(@RequestBody Map<String, Object> info) throws Exception {
		JSONObject jso = new JSONObject(info);
		String nombre = jso.optString("nombre");
		String dosisT = jso.optString("dosisTotales");
		int dosisTotales = Integer.parseInt(dosisT);
		int aforo = 0;
		String localidad = jso.optString("localidad");
		String provincia = jso.optString("provincia");
		int hInicio = 0;
		int hFin = 0;
		List<CentroSanitario> listCentro = repository.findAll();
		if(listCentro.size()>0) {
			 hInicio = listCentro.get(0).getHoraInicio();
			 hFin = listCentro.get(0).getHoraFin();
			 aforo = listCentro.get(0).getAforo();
		}else {
			 String af = jso.optString("aforo");
			 String horaIncicio = jso.optString("horaInicio");
			 String horaFin = jso.optString("horaFin");
			 aforo = Integer.parseInt(af);
			 hInicio = Integer.parseInt(horaIncicio);
			 hFin = Integer.parseInt(horaFin);
		}
		CentroSanitario c = new CentroSanitario(nombre, dosisTotales, aforo,hInicio,hFin, localidad, provincia);
//		if (c.getNombre().isEmpty() || c.getDosisTotales() <= 0 || c.getAforo() <= 0 || c.getLocalidad().isEmpty()
//				|| c.getProvincia().isEmpty()) {
//			throw new ResponseStatusException(HttpStatus.CONFLICT, "Rellena todos los campos correctamente");
//		}

		return repository.insert(c);

	}

	@GetMapping("/getTodos")
	public List<CentroSanitario> get() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@Transactional
	@PutMapping("/modificarCentro/{id}")
	public CentroSanitario modificarCentro(@PathVariable String id, 
			@RequestBody Map<String, Object> info)
			throws Exception {
		Optional<CentroSanitario> optCentroSanitario = repository.findById(id);

		JSONObject jso = new JSONObject(info);
		String nombre = jso.optString("nombre");
		String dosisT = jso.optString("dosisTotales");
		int dosisTotales = Integer.parseInt(dosisT);
		String af = jso.optString("aforo");
		String horaIncicio = jso.optString("horaInicio");
		String horaFin = jso.optString("horaFin");
		int aforo = Integer.parseInt(af);
		String localidad = jso.optString("localidad");
		String provincia = jso.optString("provincia");
		int hInicio = Integer.parseInt(horaIncicio);
		int hFin = Integer.parseInt(horaFin);
		
		CentroSanitario centro = new CentroSanitario();
		if (optCentroSanitario.isPresent()) {
			centro = optCentroSanitario.get();
		}
		//Modificar el aforo,HoraInicio y fin de todos los centros
		List<CentroSanitario> listCentro = repository.findAll();
		for(int i=0;i<listCentro.size();i++) {
			CentroSanitario cs = listCentro.get(i);
			listCentro.get(i).setAforo(aforo);
			listCentro.get(i).setHoraInicio(hInicio);
			listCentro.get(i).setHoraFin(hFin);
			repository.save(cs);
		}

		centro.setNombre(nombre);
		centro.setAforo(aforo);
		centro.setHoraInicio(hInicio);
		centro.setHoraFin(hFin);
		centro.setLocalidad(localidad);
		centro.setProvincia(provincia);
		centro.setDosisTotales(dosisTotales);

		return repository.save(centro);
	}

	@Transactional
	@DeleteMapping("/eliminarCentro/{id}")
	public void eliminarCentro(@PathVariable String id) {
		if (repository.findById(id)==null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El centro no existe");
		}
		List<Paciente> lista = pacienteDao.findAll();
		Iterator<Paciente> it = lista.iterator();
		Optional<CentroSanitario> cs = repository.findById(id);
		String nombreCentro = cs.get().getNombre();
		while(it.hasNext()) {
			if(it.next().getCentroAsignado().equals(nombreCentro)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,"El centro no se puede eliminar");
			}
		}	
		repository.deleteById(id);
	}

}