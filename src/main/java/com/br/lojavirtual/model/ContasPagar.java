package com.br.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.br.lojavirtual.enums.StatusContasPagar;

@Entity
@Table(name = "T_CONTAS_PAGAR")
@SequenceGenerator(name = "seq_contas_pagar", sequenceName = "seq_contas_pagar", allocationSize = 1, initialValue = 1)
public class ContasPagar implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contas_pagar")	
	private Long id;

	@DateTimeFormat(pattern = "MM-dd-yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_cadastro = new Date();

	@DateTimeFormat(pattern = "MM-dd-yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_pagamento;

	@NotNull(message = "Campo Data de vencimento obrigatório")
	@Column(nullable = false)
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_vencimento;	
	
	@ManyToOne(targetEntity = PessoaFisica.class)
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private PessoaFisica pessoaId;
	
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "pessoa_forn_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fornecedor_fk"))
	private PessoaJuridica pessoa_fornecedor;
	
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_cp_fk"))
	private PessoaJuridica empresaId;	
	
	@NotNull(message = "Campo Status obrigatório")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusContasPagar statusContasPagar;
	
	private BigDecimal valor_desconto;
	
	@NotNull(message = "Campo valor obrigatório")
	@Column(nullable = false)
    private BigDecimal valor_total;

	@NotNull(message = "Campo descrição obrigatório")
    @Column(columnDefinition = "text", nullable = false)
    private String descricao;
	
	@Column(columnDefinition = "text", length = 2000)
    private String observacao;

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

	public Date getData_pagamento() {
		return data_pagamento;
	}

	public void setData_pagamento(Date data_pagamento) {
		this.data_pagamento = data_pagamento;
	}

	public Date getData_vencimento() {
		return data_vencimento;
	}

	public void setData_vencimento(Date data_vencimento) {
		this.data_vencimento = data_vencimento;
	}

	public BigDecimal getValor_desconto() {
		return valor_desconto;
	}

	public void setValor_desconto(BigDecimal valor_desconto) {
		this.valor_desconto = valor_desconto;
	}

	public BigDecimal getValor_total() {
		return valor_total;
	}

	public void setValor_total(BigDecimal valor_total) {
		this.valor_total = valor_total;
	}	

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}	

	public StatusContasPagar getStatusContasPagar() {
		return statusContasPagar;
	}

	public void setStatusContasPagar(StatusContasPagar statusContasPagar) {
		this.statusContasPagar = statusContasPagar;
	}	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	

	public PessoaFisica getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(PessoaFisica pessoaId) {
		this.pessoaId = pessoaId;
	}

	public PessoaJuridica getPessoa_fornecedor() {
		return pessoa_fornecedor;
	}

	public void setPessoa_fornecedor(PessoaJuridica pessoa_fornecedor) {
		this.pessoa_fornecedor = pessoa_fornecedor;
	}

	public PessoaJuridica getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(PessoaJuridica empresaId) {
		this.empresaId = empresaId;
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
		ContasPagar other = (ContasPagar) obj;
		return Objects.equals(id, other.id);
	} 	
    		
}
