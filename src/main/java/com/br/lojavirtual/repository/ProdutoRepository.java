package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(descricao)) = upper(trim(?1))")
	public boolean existeProduto(String descricao);

//	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(descricao)) = upper(trim(?1)) and empresa_id = ?2")
//	public boolean existeProduto(String descricao, Long EmpresaId);

	@Query("select a from Produto a where upper(trim(a.descricao)) like %?1%")
	public List<Produto> buscarProdutoDescricao(String descricao);
	
	@Query("select a from Produto a where upper(trim(a.descricao)) like %?1% and a.empresaId.id = ?2")
	public List<Produto> buscarProdutoDescricao(String descricao, Long EmpresaId);	
}
