package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class CobrancaGeradaAsaasInterest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Double value;
	private String type;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	

}
