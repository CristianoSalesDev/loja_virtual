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

import org.springframework.format.annotation.DateTimeFormat;

import com.br.lojavirtual.enums.TipoUnidade;

@Entity
@Table(name = "T_PRODUTO")
@SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")	
	private Long id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date data_cadastro = new Date();
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoUnidade tipoUnidade;
		
	@Column(nullable = false)
	private String descricao;
	
	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "empresa_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_prod_fk"))
	private Pessoa empresaId;	
	
	
	@Column(columnDefinition = "text", length = 2000)
	private String detalhes;
	
	@Column(nullable = false)
    private Double peso;
    
	@Column(nullable = false)
    private Double altura;
    
	@Column(nullable = false)
    private Double largura;
    
	@Column(nullable = false)
    private Double comprimento;
    
	@Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;
    
	@Column(nullable = false)
    private Integer QtdeEstoque = 0;
    
    private Integer QtdeAlertaEstoque = 0;
    
    private String link;
    
    private Boolean AlertaQtdeEstoque = Boolean.FALSE;
    
    private Integer QtdeClique = 0;
	
    @Column(nullable = false)
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

	public TipoUnidade getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(TipoUnidade tipoUnidade) {
		this.tipoUnidade = tipoUnidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Double getLargura() {
		return largura;
	}

	public void setLargura(Double largura) {
		this.largura = largura;
	}

	public Double getComprimento() {
		return comprimento;
	}

	public void setComprimento(Double comprimento) {
		this.comprimento = comprimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getQtdeEstoque() {
		return QtdeEstoque;
	}

	public void setQtdeEstoque(Integer qtdeEstoque) {
		QtdeEstoque = qtdeEstoque;
	}

	public Integer getQtdeAlertaEstoque() {
		return QtdeAlertaEstoque;
	}

	public void setQtdeAlertaEstoque(Integer qtdeAlertaEstoque) {
		QtdeAlertaEstoque = qtdeAlertaEstoque;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getAlertaQtdeEstoque() {
		return AlertaQtdeEstoque;
	}

	public void setAlertaQtdeEstoque(Boolean alertaQtdeEstoque) {
		AlertaQtdeEstoque = alertaQtdeEstoque;
	}

	public Integer getQtdeClique() {
		return QtdeClique;
	}

	public void setQtdeClique(Integer qtdeClique) {
		QtdeClique = qtdeClique;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}	

	public Pessoa getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(Pessoa empresaId) {
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
		Produto other = (Produto) obj;
		return Objects.equals(id, other.id);
	}

}
