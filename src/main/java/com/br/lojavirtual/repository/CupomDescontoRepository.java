package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.CupomDesconto;

@Repository
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {

	@Query(value = "select c from CupomDesconto c where c.empresaId.id = ?1")
	public List<CupomDesconto> cupomDescontoPorEmpresa(Long idEmpresa);
	
}
