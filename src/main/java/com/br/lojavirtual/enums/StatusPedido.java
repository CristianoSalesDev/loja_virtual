package com.br.lojavirtual.enums;

public enum StatusPedido {

    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada"),
    ABANDONOU_CARRINHO("Abandonou Carrinho");
    
    private String descricao;
	
    private StatusPedido(String descricao) {
    	this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

   	@Override
   	public String toString() {
   		// TODO Auto-generated method stub
   		return this.descricao;
   	}
    
    
}

