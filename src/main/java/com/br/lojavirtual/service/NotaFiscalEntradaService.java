package com.br.lojavirtual.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.br.lojavirtual.model.dto.ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO;
import com.br.lojavirtual.model.dto.ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO;
import com.br.lojavirtual.model.dto.ObjetoRelatorioStatusCompra;

@Service
public class NotaFiscalEntradaService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<ObjetoRelatorioStatusCompra> relatorioStatusVendaLojaVirtual(ObjetoRelatorioStatusCompra objetoRelatorioStatusCompra){
		
		List<ObjetoRelatorioStatusCompra> retorno = new ArrayList<ObjetoRelatorioStatusCompra>();
		
		String sql = "select prod.id as codigoProduto, "
				+ " prod.descricao as nomeProduto, "
				+ " pf.email as emailCliente, "
				+ " pf.telefone as foneCliente, "
				+ " prod.valor as valor, "
				+ " pf.id as codigoCliente, "
				+ " pf.nome as nomeCliente,"
				+ " prod.qtde_estoque as qtdEstoque, "
				+ " ped.id as codigoVenda, "
				+ " ped.status_pedido as statusVenda "
				+ " from t_pedido as ped "
				+ " inner join t_item_pedido ip on (ped.id = ip.pedido_id) "
				+ " inner join t_produto as prod on (prod.id = ip.produto_id) "
				+ " inner join t_pessoa_fisica as pf on (pf.id = ped.pessoa_id) ";
		
		
				sql+= " where ped.data_venda >= '"+objetoRelatorioStatusCompra.getDataInicial()+"' and ped.data_venda  <= '"+objetoRelatorioStatusCompra.getDataFinal()+"' ";
				
				if(!objetoRelatorioStatusCompra.getNomeProduto().isEmpty()) {		
				  sql += " and upper(prod.descricao) like upper('%"+objetoRelatorioStatusCompra.getNomeProduto()+"%') ";
				}
				
				if (!objetoRelatorioStatusCompra.getStatusVenda().isEmpty()) {
				 sql+= " and ped.status_pedido in ('"+objetoRelatorioStatusCompra.getStatusVenda()+"') ";
				}
				
				if (!objetoRelatorioStatusCompra.getNomeCliente().isEmpty()) {
				 sql += " and pf.nome like '%"+objetoRelatorioStatusCompra.getNomeCliente()+"%' ";
				}
		
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ObjetoRelatorioStatusCompra.class));
				
		return retorno;
		
	}
	

	/**
	 * Title: Histórico de compras de produtor para a loja.
	 * 
	 * Este relatório permite saber os produtos comprados para serem vendido pela loja virtual, todos os produtos tem relação com a 
	 * nota fiscal de compra/venda.
	 * @param obejtoRequisicaoRelatorioProdCompraNotaFiscalDto
	 * @param dataInicio e dataFinal são parametros obrigatórios
	 * @return List<ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO>
	 * 
	 * @author Cristiano Aragão
	 */
	public List<ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO> gerarRelatorioProdutosCompraNota(
			ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto) {
		
		List<ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO> retorno = new ArrayList<ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO>();
		
		String sql = "select prod.id as codigoProduto, prod.descricao as nomeProduto, "
				+ " prod.valor as valor, ie.quantidade as quantidade, ne.numero_nota as codigoNota, "
				+ " pj.id as codigofornecedor, pj.nome nomeFornecedor,ne.data_entrada as dataCompra "
				+ " from t_nota_entrada as ne "
				+ " inner join t_item_entrada as ie on (ne.id = ie.nota_entrada_id) "
				+ " inner join t_produto as prod on (prod.id = ie.produto_id) "
				+ " inner join t_pessoa_juridica as pj on (pj.id = ne.pessoa_id) where ";
		
		sql += " ne.data_entrada >='"+obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getDataInicial()+"' and ";
		sql += " ne.data_entrada <= '" + obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getDataFinal() +"' ";
		
		if (!obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getCodigoNota().isEmpty()) {
		 sql += " and ne.id = " + obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getCodigoNota() + " ";
		}
		
		if (!obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getCodigoProduto().isEmpty()) {
			sql += " and prod.id = " + obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getCodigoProduto() + " ";
		}
		
		if (!obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getNomeProduto().isEmpty()) {
			sql += " upper(prod.descricao) like upper('%"+obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getNomeProduto()+"')";
		}
		
		if (!obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getNomeFornecedor().isEmpty()) {
			sql += " upper(pj.nome) like upper('%"+obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto.getNomeFornecedor()+"')";
		}
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO>(ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO.class));
		
		return retorno;
	}
	
	/**
	 * Este relatório retorna os produtos que estão com estoque  menor ou igual a quantidade definida no campo de qtde_alerta_estoque.
	 * @param alertaEstoque ObejtoRequisicaoRelatorioProdutoAlertaEstoque
	 * @return  List<ObejtoRequisicaoRelatorioProdutoAlertaEstoque>  Lista de Objetos ObejtoRequisicaoRelatorioProdutoAlertaEstoque
	 */
	public List<ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO> 
					gerarRelatorioAlertaEstoque(ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO alertaEstoque ){
		
		List<ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO> retorno = new ArrayList<ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO>();
		
		String sql = "select prod.id as codigoProduto, prod.descricao as nomeProduto, "
				+ " prod.valor as valor, ie.quantidade as quantidade, ne.numero_nota as codigoNota, "
				+ " pj.id as codigofornecedor, pj.nome nomeFornecedor,ne.data_entrada as dataCompra, "
				+ " prod.qtde_estoque as qtdeEstoque, prod.qtde_alerta_estoque as qtdeAlertaEstoque "
				+ " from t_nota_entrada as ne "
				+ " inner join t_item_entrada as ie on (ne.id = ie.nota_entrada_id) "
				+ " inner join t_produto as prod on (prod.id = ie.produto_id) "
				+ " inner join t_pessoa_juridica as pj on (pj.id = ne.pessoa_id) where ";				
		
		sql += " ne.data_entrada >='"+alertaEstoque.getDataInicial()+"' and ";
		sql += " ne.data_entrada <= '" + alertaEstoque.getDataFinal() +"' ";
		sql += " and prod.alerta_qtde_estoque = true and prod.qtde_estoque <= prod.qtde_alerta_estoque "; 
		
		if (!alertaEstoque.getCodigoNota().isEmpty()) {
		 sql += " and ne.id = " + alertaEstoque.getCodigoNota() + " ";
		}
		
		if (!alertaEstoque.getCodigoProduto().isEmpty()) {
			sql += " and prod.id = " + alertaEstoque.getCodigoProduto() + " ";
		}
		
		if (!alertaEstoque.getNomeProduto().isEmpty()) {
			sql += " upper(prod.nome) like upper('%"+alertaEstoque.getNomeProduto()+"')";
		}
		
		if (!alertaEstoque.getNomeFornecedor().isEmpty()) {
			sql += " upper(pj.nome) like upper('%"+alertaEstoque.getNomeFornecedor()+"')";
		}
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO>(ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO.class));
		
		return retorno;
	}	
}
