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

	public Produto getProduto() {
		return produtoId;
	}

	public void setProduto(Produto produto) {
		this.produtoId = produto;
	}

	
}
