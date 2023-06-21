package com.br.lojavirtual.enums;

public enum StatusContasReceber {

       ABERTA("Aberta"),
       LIQUIDADA("Liquidada"),
       CANCELADA("Cancelada");
       
   	   private String descricao;
	
   	   private StatusContasReceber(String descricao) {
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
