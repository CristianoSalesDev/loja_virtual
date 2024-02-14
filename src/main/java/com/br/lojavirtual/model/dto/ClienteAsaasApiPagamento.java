package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class ClienteAsaasApiPagamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	private String cpfCnpj;
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
