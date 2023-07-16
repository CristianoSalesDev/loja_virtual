package com.br.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long>  {
	
	@Query("select u from Usuario u where upper(trim(u.login)) like %?1%")
	Usuario findUsuarioByLogin(String login);	

}
