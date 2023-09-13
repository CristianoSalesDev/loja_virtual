package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.lojavirtual.model.ContasPagar;

@Repository
@Transactional
public interface ContasPagarRepository extends JpaRepository<ContasPagar, Long> {

	@Query("select a from ContasPagar a where upper(trim(a.descricao)) like %?1%")
	List<ContasPagar> buscarContasPagarDescricao(String descricao);
	
	
	@Query("select a from ContasPagar a where a.pessoaId.id = ?1")
	List<ContasPagar> buscarContasPagarPorPessoa(Long PessoaId);
	
	
	@Query("select a from ContasPagar a where a.pessoa_fornecedor.id = ?1")
	List<ContasPagar> buscarContasPagarPorFornecedor(Long PessoaForncedorId);

	
	@Query("select a from ContasPagar a where a.empresaId.id = ?1")
	List<ContasPagar> buscarContasPagarPorEmpresa(Long EmpresaId);	
}
