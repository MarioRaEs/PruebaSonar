package com.vacuna.vacuna.model;

import java.util.UUID;
import org.springframework.data.annotation.Id;

public class Cita {
	
	@Id 
	private String id ;
	private long fechaPrimeraDosis;
	private long fechaSegundaDosis;
	private String nombreCentro;
	private String dniPaciente;
	
	public Cita(long fechaPrimeraDosis, long fechaSegundaDosis, String nombreCentro, String dniPaciente) {
		super();
		this.fechaPrimeraDosis = fechaPrimeraDosis;
		this.fechaSegundaDosis = fechaSegundaDosis;
		this.nombreCentro = nombreCentro;
		this.dniPaciente = dniPaciente;
	}

	public String getDniPaciente() {
		return dniPaciente;
	}

	public void setDniPaciente(String dniPaciente) {
		this.dniPaciente = dniPaciente;
	}


	public String getNombreCentro() {
		return nombreCentro;
	}

	public void setNombreCentro(String nombreCentro) {
		this.nombreCentro = nombreCentro;
	}

	public Cita() {
		this.id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public long getFechaPrimeraDosis() {
		return fechaPrimeraDosis;
	}

	public void setFechaPrimeraDosis(long fechaPrimeraDosis) {
		this.fechaPrimeraDosis = fechaPrimeraDosis;
	}

	public long getFechaSegundaDosis() {
		return fechaSegundaDosis;
	}

	public void setFechaSegundaDosis(long fechaSegundaDosis) {
		this.fechaSegundaDosis = fechaSegundaDosis;
	}
	
	
	
}
