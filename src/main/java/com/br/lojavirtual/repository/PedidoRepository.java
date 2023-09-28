package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.lojavirtual.model.Pedido;

@Repository
@Transactional
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	@Query(value = "select p from Pedido p where p.id = ?1 and p.excluido = false")
	Pedido findByIdExclusao(Long id); 
	
	@Query(value="select i.pedidoId from ItemPedido i where "
			+ " i.pedidoId.excluido = false and i.produtoId.id = ?1")
	List<Pedido> consultaPorProduto(Long idProduto);
	
	@Query(value="select distinct(i.pedidoId) from ItemPedido i "
			+ " where i.pedidoId.excluido = false and upper(trim(i.produtoId.descricao)) like %?1%")
	List<Pedido> consultaPorNomeProduto(String nomeproduto);

	@Query(value="select distinct(i.pedidoId) from ItemPedido i "
			+ " where i.pedidoId.excluido = false and upper(trim(i.pedidoId.pessoa.nome)) like %?1%")
	List<Pedido> consultaPorNomeCliente(String nomepessoa);

	@Query(value="select distinct(i.pedidoId) from ItemPedido i "
			+ " where i.pedidoId.excluido = false and upper(trim(i.pedidoId.enderecoCobranca.logradouro)) "
			+ " like %?1%")
	List<Pedido> consultaPorEnderecoCobranca(String enderecocobranca);

	
	@Query(value="select distinct(i.pedidoId) from ItemPedido i "
			+ " where i.pedidoId.excluido = false and upper(trim(i.pedidoId.enderecoEntrega.logradouro)) "
			+ " like %?1%")
	List<Pedido> consultaPorEnderecoEntrega(String enderecoentrega);	
	
}
