package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class ObjetoErroResponseApiAsaas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

}
