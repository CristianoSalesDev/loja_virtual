package com.br.lojavirtual.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.br.lojavirtual.model.Endereco;
import com.br.lojavirtual.model.Pessoa;

public class PedidoDTO {

	private Long id;

	private BigDecimal valorTotal;

	private BigDecimal valorDesconto;

	private Pessoa pessoa;

	private Endereco cobranca;

	private Endereco entrega;

	private BigDecimal valorFrete;

	private List<ItemPedidoDto> itemPedido = new ArrayList<ItemPedidoDto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Endereco getCobranca() {
		return cobranca;
	}

	public void setCobranca(Endereco cobranca) {
		this.cobranca = cobranca;
	}

	public Endereco getEntrega() {
		return entrega;
	}

	public void setEntrega(Endereco entrega) {
		this.entrega = entrega;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public List<ItemPedidoDto> getItemPedido() {
		return itemPedido;
	}

	public void setItemPedido(List<ItemPedidoDto> itemPedido) {
		this.itemPedido = itemPedido;
	}
	
	
}
