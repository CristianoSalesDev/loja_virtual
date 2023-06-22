package com.br.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "T_CUPOM_DESCONTO")
@SequenceGenerator(name = "seq_cupom", sequenceName = "seq_cupom", allocationSize = 1, initialValue = 1)
public class CupomDesconto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cupom")	
	private Long id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_cadastro = new Date();
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_validade;
	
	@Column(nullable = false)
	private String codigo_cupom;
	
	private BigDecimal valor_real_desconto;
	
    private BigDecimal percentual_desconto;
	
    private Boolean ativo = Boolean.TRUE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData_cadastro() {
		return data_cadastro;
	}

	public void setData_cadastro(Date data_cadastro) {
		this.data_cadastro = data_cadastro;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Date getData_validade() {
		return data_validade;
	}

	public void setData_validade(Date data_validade) {
		this.data_validade = data_validade;
	}

	public String getCodigo_cupom() {
		return codigo_cupom;
	}

	public void setCodigo_cupom(String codigo_cupom) {
		this.codigo_cupom = codigo_cupom;
	}

	public BigDecimal getValor_real_desconto() {
		return valor_real_desconto;
	}

	public void setValor_real_desconto(BigDecimal valor_real_desconto) {
		this.valor_real_desconto = valor_real_desconto;
	}

	public BigDecimal getPercentual_desconto() {
		return percentual_desconto;
	}

	public void setPercentual_desconto(BigDecimal percentual_desconto) {
		this.percentual_desconto = percentual_desconto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CupomDesconto other = (CupomDesconto) obj;
		return Objects.equals(id, other.id);
	}

}
