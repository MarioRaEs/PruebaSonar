package com.vacuna.vacuna.DAO;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vacuna.vacuna.model.Usuario;

@Repository
public interface UsuarioDAO extends MongoRepository<Usuario, String> {

	Usuario findByDni(String dni);

	Usuario deleteByDni(String dni);

	public List<Usuario> findAll();


	Usuario findByEmailAndPassword(String email, byte[] pwd);

}
