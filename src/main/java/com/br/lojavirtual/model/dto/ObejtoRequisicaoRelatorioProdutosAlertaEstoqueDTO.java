package com.br.lojavirtual.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO implements Serializable {

	private static final long serialVersionUID = 1L;   
	
	@NotEmpty(message = "Informe a data inicial")
	private String dataInicial;
	
	@NotEmpty(message = "Informe a data final")
	private String dataFinal;
	
	private String nomeProduto ="";
	private String codigoNota ="";
	private String codigoProduto ="";
	private String valor ="";
	private String quantidade ="";
	private String codigoFornecedor ="";
	private String nomeFornecedor ="";
	private String dataCompra ="";
	private String qtdeEstoque ="";
	private String qtdeAlertaEstoque ="";
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}
	public String getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	public String getCodigoNota() {
		return codigoNota;
	}
	public void setCodigoNota(String codigoNota) {
		this.codigoNota = codigoNota;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}
	public String getCodigoFornecedor() {
		return codigoFornecedor;
	}
	public void setCodigoFornecedor(String codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	public String getDataCompra() {
		return dataCompra;
	}
	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}
	public String getQtdeEstoque() {
		return qtdeEstoque;
	}
	public void setQtdeEstoque(String qtdeEstoque) {
		this.qtdeEstoque = qtdeEstoque;
	}
	public String getQtdeAlertaEstoque() {
		return qtdeAlertaEstoque;
	}
	public void setQtdeAlertaEstoque(String qtdeAlertaEstoque) {
		this.qtdeAlertaEstoque = qtdeAlertaEstoque;
	}	
	
}
