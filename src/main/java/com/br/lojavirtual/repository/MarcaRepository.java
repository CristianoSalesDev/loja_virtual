package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.lojavirtual.model.Marca;

@Repository
@Transactional
public interface MarcaRepository extends JpaRepository<Marca, Long> {

	@Query("select a from Marca a where upper(trim(a.descricao)) like %?1%")
	List<Marca> buscarMarcaDescricao(String descricao);
	
}
