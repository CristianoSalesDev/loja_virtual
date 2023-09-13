package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class CategoriaDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String descricao;

	private String empresaId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(String empresaId) {
		this.empresaId = empresaId;
	}
	
}
