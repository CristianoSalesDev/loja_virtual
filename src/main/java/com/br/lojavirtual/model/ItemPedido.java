package com.br.lojavirtual.model;

import java.io.Serializable;
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
@Table(name = "T_ITEM_PEDIDO")
@SequenceGenerator(name = "seq_item_pedido", sequenceName = "seq_item_pedido", allocationSize = 1, initialValue = 1)

public class ItemPedido implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_pedido")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
	private Produto produto_id;

	@ManyToOne
	@JoinColumn(name = "pedido_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pedido_fk"))    
    private Pedido pedidoId;
	
	@Column(nullable = false)
    private Double quantidade;

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

	public Pedido getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Pedido pedidoId) {
		this.pedidoId = pedidoId;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
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
		ItemPedido other = (ItemPedido) obj;
		return Objects.equals(id, other.id);
	}
    
   
	
}
