package com.br.lojavirtual.enums;

public enum TipoUnidade {

	UNIDADE("UND"),
	SACO("SAC"),
	CAIXA("CXA"),
	FARDO("FDO"),
	PACOTE("PCT"),
	DUZIA("DUZ"),
	KILO("KG"),
	LATA("LTA"),
	POTE("PT"),
	CARTELA("CRT"),
	DEZENA("DEZ"),
	CENTENA("CEN"),
	CAIXETA("CXT"),
	GALAO("GAL"),
	LITRO("LT");
	
	private String descricao;
	
	
	private TipoUnidade(String descricao) {
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
