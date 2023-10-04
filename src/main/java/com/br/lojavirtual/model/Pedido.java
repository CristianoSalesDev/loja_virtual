package com.br.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "T_PEDIDO")
@SequenceGenerator(name = "seq_pedido", sequenceName = "seq_pedido", allocationSize = 1, initialValue = 1)
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pedido")	
	private Long id;
	
	@NotNull(message = "Data da venda deve ser informada")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_cadastro = new Date();

	@NotNull(message = "Data da entrega deve ser informada")
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataEntrega;
	
	@NotNull(message = "Data da venda deve ser informada")
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataVenda;	
	
	@NotNull(message = "Cliente deve ser informado")
	@ManyToOne(targetEntity = PessoaFisica.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private PessoaFisica pessoa;
	
	@NotNull(message = "Fornecedor deve ser informada")
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_ped_fk"))
	private PessoaJuridica empresaId;
	
	@JsonIgnoreProperties(allowGetters = true)
	@NotNull(message = "A nota fiscal deve ser informada")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "nfe_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "nfe_fk"))    
    private NFe nfeId;
	
	@OneToMany(mappedBy = "pedidoId", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ItemPedido> itemPedidoId = new ArrayList<ItemPedido>();
	
	@NotNull(message = "O endereço de entrega deve ser informado")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_entrega_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "endereco_entrega_fk"))	
	private Endereco enderecoEntrega;
	
	@NotNull(message = "O endereço de entrega deve ser informado")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_cobranca_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "endereco_cobranca_fk"))	
	private Endereco enderecoCobranca;

	@Min(value = 1, message = "Valor total da venda é invalida")
	@Column(nullable = false)
    private BigDecimal valorTotal;
    
    private BigDecimal valorDesconto;

	@Min(value = 5, message = "valor do frete é inválido")
	@NotNull(message = "O valor do frete de ser informado")
    @Column(nullable = false)
    private BigDecimal valorFrete;

	@Min(value = 1, message = "Dia de entrega é inválido")
    @Column(nullable = false)
    private Integer diasEntrega;    
    
    @NotNull(message = "A forma de pagamento deve ser informado")
	@ManyToOne
	@JoinColumn(name = "forma_pagamento_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "forma_pagamento_fk"))    
    private FormaPagamento formaPagamentoId;    
		
	@ManyToOne
	@JoinColumn(name = "cupom_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "cupom_fk"))    
    private CupomDesconto cupomId;

	@Column(columnDefinition = "text", nullable = true)
	private String observacao;
	
	private Boolean excluido = Boolean.FALSE;

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

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public PessoaFisica getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaFisica pessoa) {
		this.pessoa = pessoa;
	}

	public void setEmpresaId(PessoaJuridica empresaId) {
		this.empresaId = empresaId;
	}

	public Endereco getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(Endereco enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public Endereco getEnderecoCobranca() {
		return enderecoCobranca;
	}

	public void setEnderecoCobranca(Endereco enderecoCobranca) {
		this.enderecoCobranca = enderecoCobranca;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public Integer getDiasEntrega() {
		return diasEntrega;
	}

	public void setDiasEntrega(Integer diasEntrega) {
		this.diasEntrega = diasEntrega;
	}

	public FormaPagamento getFormaPagamentoId() {
		return formaPagamentoId;
	}

	public void setFormaPagamentoId(FormaPagamento formaPagamentoId) {
		this.formaPagamentoId = formaPagamentoId;
	}

	public NFe getNfeId() {
		return nfeId;
	}

	public void setNfeId(NFe nfeId) {
		this.nfeId = nfeId;
	}

	public CupomDesconto getCupomId() {
		return cupomId;
	}

	public void setCupomId(CupomDesconto cupomId) {
		this.cupomId = cupomId;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public PessoaJuridica getEmpresaId() {
		return empresaId;
	}	

	public List<ItemPedido> getItemPedidoId() {
		return itemPedidoId;
	}

	public void setItemPedidoId(List<ItemPedido> itemPedidoId) {
		this.itemPedidoId = itemPedidoId;
	}	

	public Boolean getExcluido() {
		return excluido;
	}

	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
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
		Pedido other = (Pedido) obj;
		return Objects.equals(id, other.id);
	}	
	
}
