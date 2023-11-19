package com.br.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.BoletoJuno;

@Repository
public interface BoletoJunoRepository extends JpaRepository<BoletoJuno, Long> {
	
	

}
