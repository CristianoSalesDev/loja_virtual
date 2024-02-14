package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class CobrancaGeradaAsaasDiscount implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double value;
	private String limitDate;
	private Integer dueDateLimitDays;
	private String type;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}

	public Integer getDueDateLimitDays() {
		return dueDateLimitDays;
	}

	public void setDueDateLimitDays(Integer dueDateLimitDays) {
		this.dueDateLimitDays = dueDateLimitDays;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
