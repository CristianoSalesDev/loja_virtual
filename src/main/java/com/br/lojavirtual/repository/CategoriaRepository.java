package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	@Query(nativeQuery = true, value = "select count(1) > 0 from t_categoria where upper(trim(descricao)) = upper(trim(?1))")
	public boolean existeCategoria(String nomeCategoria);

	
	@Query("select a from Categoria a where upper(trim(a.descricao)) like %?1%")
	public List<Categoria> buscarCategoriaDes(String descricao);
}
