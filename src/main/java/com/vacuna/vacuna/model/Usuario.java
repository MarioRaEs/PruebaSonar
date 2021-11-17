package com.vacuna.vacuna.model;

import javax.persistence.Lob;

import org.springframework.data.annotation.Id;

public class Usuario {

	protected String nombre;
	protected String email;
	@Lob
	protected byte[] password;
	@Id
	protected String dni;
	protected String tipoUsuario;

	public Usuario(String nombre, String email, byte[] password, String dni, String tipoUsuario) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.dni = dni;
		this.tipoUsuario = tipoUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

}
