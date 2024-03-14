package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.FormaPagamento;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

	@Query(value = "select f from FormaPagamento f where f.empresaId.id = ?1")
	List<FormaPagamento> findAll(Long idEmpresa);
	
    @Query("select fp from FormaPagamento fp where fp.empresaId.id = ?1")
    public List<FormaPagamento> findPorPage(Long idEmpresa , Pageable pageable);
	
    @Query(nativeQuery = true, value = "select cast((count(1) /5) as integer) + 1 as qtdpagina from t_forma_pagamento fp where fp.empresa_id = ?1")
    public Integer qtdPagina(Long idEmpresa);
	
    @Query("select fp from FormaPagamento fp where upper(trim(fp.descricao)) like %?1% and fp.empresaId.id = ?2")
	public List<FormaPagamento> buscarFormaPagamentoDes(String descricao, Long empresaId);
    
    
}
