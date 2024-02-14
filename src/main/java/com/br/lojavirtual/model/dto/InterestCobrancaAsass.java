package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class InterestCobrancaAsass implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private float value;
	
	public void setValue(float value) {
		this.value = value;
	}
	public float getValue() {
		return value;
	}
	
}
