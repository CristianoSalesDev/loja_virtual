package com.br.lojavirtual.model.dto;

import com.br.lojavirtual.model.Produto;

public class ItemPedidoDto {

	private Double quantidade;

	private Produto produtoId;

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Produto produtoId) {
		this.produtoId = produtoId;
	}

}
