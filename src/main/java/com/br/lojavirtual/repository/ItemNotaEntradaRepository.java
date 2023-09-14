package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.lojavirtual.model.ItemEntrada;
import com.br.lojavirtual.model.NotaEntrada;

@Repository
@Transactional
public interface ItemNotaEntradaRepository extends JpaRepository<ItemEntrada, Long> {

	@Query("select a from ItemEntrada a where a.produtoId.id = ?1 and a.notaEntradaId.id = ?2")
	List<ItemEntrada> buscaItemEntradaPorProdutoNota(Long produtoId, Long notaEntradaId);
	
	
	@Query("select a from ItemEntrada a where a.produtoId.id = ?1")
	List<ItemEntrada> buscaItemEntradaPorProduto(Long produtoId);
	
	
	@Query("select a from ItemEntrada a where a.notaEntradaId.id = ?2")
	List<ItemEntrada> buscaItemEntradaPorNotaFiscal(Long notaEntradaId);
	
	
	@Query("select a from ItemEntrada a where a.empresaId.id = ?2")
	List<NotaEntrada> buscaItemEntradaPorEmpresa(Long empresaId);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "delete from t_item_entrada where id = ?1")
	void deleteByIdItemEntrada(Long id);	
}
