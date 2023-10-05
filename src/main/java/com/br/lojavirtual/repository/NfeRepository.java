package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.NFe;

@Repository
public interface NfeRepository extends JpaRepository<NFe, Long> {

	@Query(value = "select n from NFe n where n.pedidoId.id = ?1")
	List<NFe> consultarNotaFiscalPorVenda(Long idPedido);
		
	@Query(value = "select n from NFe n where n.pedidoId.id = ?1")
	NFe consultarNotaFiscalPorVendaUnica(Long idPedido);	
}
