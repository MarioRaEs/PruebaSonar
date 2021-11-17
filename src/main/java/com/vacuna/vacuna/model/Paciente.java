package com.vacuna.vacuna.model;

public class Paciente extends Usuario {

	private String centroAsignado;
	private String dosisAdministradas;
	private String localidad;
	private String provincia;

	public Paciente(String nombre, String email, byte[] password, String dni, String tipoUsuario, String centroAsignado,
			String dosisAdministradas, String localidad, String provincia) {
		super(nombre, email, password, dni, tipoUsuario);
		this.centroAsignado = centroAsignado;
		this.dosisAdministradas = dosisAdministradas;
		this.localidad = localidad;
		this.provincia = provincia;
	}

	public String getCentroAsignado() {
		return centroAsignado;
	}

	public void setCentroAsignado(String centroAsignado) {
		this.centroAsignado = centroAsignado;
	}

	public String getDosisAdministradas() {
		return dosisAdministradas;
	}

	public void setDosisAdministradas(String dosisAdministradas) {
		this.dosisAdministradas = dosisAdministradas;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

}
