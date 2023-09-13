package com.br.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "T_NOTA_ENTRADA")
@SequenceGenerator(name = "seq_nota_entrada", sequenceName = "seq_nota_entrada", allocationSize = 1, initialValue = 1)

public class NotaEntrada implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nota_entrada")	
	private Long id;

	@DateTimeFormat(pattern = "MM-dd-yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_entrada = new Date();

	@NotNull(message = "Informe a data da compra")
	@Column(nullable = false)
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_emissao;

	@NotNull(message = "Informe o número da nota")
	@Column(nullable = false)
    private String numeroNota;
    
    private String selo;
    
    private String chave;
    
	@NotEmpty(message = "Informe a serie da nota")
	@NotNull(message = "Informe a série da nota")
	@Column(nullable = false)
    private String serie;
    
    private String observacao;
    
    private BigDecimal valorDesconto;

//    @Size(min = 1, message = "Informe o total da nota maior que R$ 1 real")    
	@NotNull(message = "Informe o valor do ICMS")
	@Column(nullable = false)
    private BigDecimal valorIcms;
    
    private BigDecimal valorIpi;
    
    private BigDecimal valorFrete;
    
  //  @Size(min = 1, message = "Informe o total da nota maior que R$ 1 real")
	@NotNull(message = "Informe o total da nota")
	@Column(nullable = false)
    private BigDecimal valorTotal;    
	
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private PessoaJuridica pessoa;
	
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_ne_fk"))
	private PessoaJuridica empresaId;	
	
	@ManyToOne
	@JoinColumn(name = "contasPagarId", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "contasPagar_fk"))
	private ContasPagar contasPagarId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData_entrada() {
		return data_entrada;
	}

	public void setData_entrada(Date data_entrada) {
		this.data_entrada = data_entrada;
	}

	public Date getData_emissao() {
		return data_emissao;
	}

	public void setData_emissao(Date data_emissao) {
		this.data_emissao = data_emissao;
	}

	public String getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}

	public String getSelo() {
		return selo;
	}

	public void setSelo(String selo) {
		this.selo = selo;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorIcms() {
		return valorIcms;
	}

	public void setValorIcms(BigDecimal valorIcms) {
		this.valorIcms = valorIcms;
	}

	public BigDecimal getValorIpi() {
		return valorIpi;
	}

	public void setValorIpi(BigDecimal valorIpi) {
		this.valorIpi = valorIpi;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public ContasPagar getContasPagarId() {
		return contasPagarId;
	}

	public void setContasPagarId(ContasPagar contasPagarId) {
		this.contasPagarId = contasPagarId;
	}	

	public PessoaJuridica getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaJuridica pessoa) {
		this.pessoa = pessoa;
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
		NotaEntrada other = (NotaEntrada) obj;
		return Objects.equals(id, other.id);
	}
	
}
