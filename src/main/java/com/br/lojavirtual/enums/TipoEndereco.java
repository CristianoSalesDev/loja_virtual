package com.br.lojavirtual.enums;

public enum TipoEndereco {

	COBRANCA("Cobrança"),
	ENTREGA("Entrega");
	
	private String descricao;
	
	private TipoEndereco(String descricao) {
		// TODO Auto-generated constructor stub
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.descricao;
	}
}
