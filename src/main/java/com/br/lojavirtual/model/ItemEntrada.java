package com.br.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Entity
@Table(name = "T_ITEM_ENTRADA")
@SequenceGenerator(name = "seq_item_entrada", sequenceName = "seq_item_entrada", allocationSize = 1, initialValue = 1)

public class ItemEntrada implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_entrada")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
	private Produto produto_id;

	@ManyToOne
	@JoinColumn(name = "nota_entrada_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "nota_entrada_fk"))
	private NotaEntrada notaEntradaId;
	
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_ie_fk"))
	private PessoaJuridica empresaId;	
	
	@Column(nullable = false)
    private Double quantidade;
    
	@Column(nullable = false)
    private BigDecimal valorUnitario;
 
    private BigDecimal valorDesconto;
    
    @Column(nullable = false)
    private BigDecimal totalItem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Produto getProduto_id() {
		return produto_id;
	}

	public void setProduto_id(Produto produto_id) {
		this.produto_id = produto_id;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(BigDecimal totalItem) {
		this.totalItem = totalItem;
	}

	public NotaEntrada getNotaEntradaId() {
		return notaEntradaId;
	}

	public void setNotaEntradaId(NotaEntrada notaEntradaId) {
		this.notaEntradaId = notaEntradaId;
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
		ItemEntrada other = (ItemEntrada) obj;
		return Objects.equals(id, other.id);
	}   
	
}
