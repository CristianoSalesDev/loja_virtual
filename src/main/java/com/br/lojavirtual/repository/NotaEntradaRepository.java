package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.lojavirtual.model.NotaEntrada;

@Repository
@Transactional
public interface NotaEntradaRepository extends JpaRepository<NotaEntrada, Long> {

	@Query("select a from NotaEntrada a where upper(trim(a.observacao)) like %?1%")
	List<NotaEntrada> buscaObservacaoNota(String observacao);
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from t_nota_entrada where upper(observacao) like %?1% ")
	boolean existeNotaComObservacao(String observacao);
	
	@Query("select a from NotaEntrada a where a.pessoa.id = ?1")
	List<NotaEntrada> buscaNotaPorPessoa(Long pessoaId);
	
	@Query("select a from NotaEntrada a where a.contasPagarId.id = ?1")
	List<NotaEntrada> buscaNotaContaPagar(Long contaPagarId);
	
	@Query("select a from NotaEntrada a where a.empresaId.id = ?1")
	List<NotaEntrada> buscaNotaPorEmpresa(Long empresaId);
	
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, value = "delete from t_item_entrada where nota_entrada_id = ?1")
	void deleteItemNotaEntrada(Long notaEntradaId);	
}
