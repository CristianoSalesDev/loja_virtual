package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class DetailsErroJunoApi implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String field;
	private String message;
	private String errorCode;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
