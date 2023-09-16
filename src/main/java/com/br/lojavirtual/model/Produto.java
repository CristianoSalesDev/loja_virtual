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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "T_PRODUTO")
@SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")	
	private Long id;
	
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_cadastro = new Date();

	@NotNull(message = "O tipo da unidade deve ser informado")
	@Column(nullable = false)
    //@Enumerated(EnumType.STRING)
	private String tipoUnidade;

	@Size(min = 10, message = "Nome do produto deve ter mais de 10 letras")
	@NotNull(message = "Nome do produto deve ser informado")
	@Column(nullable = false)
	private String descricao;
	
	@NotNull(message = "Fornecedor deve ser informada")
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_prod_fk"))
	private PessoaJuridica empresaId;
	
	@NotNull(message = "A Categoria do Produto deve ser informada")
	@ManyToOne(targetEntity = Categoria.class)
	@JoinColumn(name = "categoria_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "categoria_id_fk"))
	private Categoria categoriaId = new Categoria();
	
	@NotNull(message = "A Marca do Produto deve ser informada")
	@ManyToOne(targetEntity = Marca.class)
	@JoinColumn(name = "marca_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "marca_id_fk"))
	private Marca marcaId = new Marca();

	@NotNull(message = "Descrição do produto deve ser informada")	
	@Column(columnDefinition = "text", length = 2000, nullable = false)
	private String detalhes;

	@NotNull(message = "Peso deve ser informado")	
	@Column(nullable = false)
    private Double peso;
    
	@NotNull(message = "Altura deve ser informado")	
	@Column(nullable = false)
    private Double altura;
    
	@NotNull(message = "Largura deve ser informado")	
	@Column(nullable = false)
    private Double largura;

	@NotNull(message = "Comprimento deve ser informado")
	@Column(nullable = false)
    private Double comprimento;
    
	@NotNull(message = "Valor de venda deve ser informado")	
	@Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;
    
	@Column(nullable = false)
    private Integer qtdeEstoque = 0;
    
    private Integer qtdeAlertaEstoque = 0;
    
    private String link;
    
    private Boolean AlertaQtdeEstoque = Boolean.FALSE;
    
    private Integer QtdeClique = 0;
	
    @Column(nullable = false)
	private Boolean ativo = Boolean.TRUE;
    
	@OneToMany(mappedBy = "produto", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ImagemProduto> imagens = new ArrayList<ImagemProduto>();	

	public List<ImagemProduto> getImagens() {
		return imagens;
	}

	public void setImagens(List<ImagemProduto> imagens) {
		this.imagens = imagens;
	}

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

	public String getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(String tipoUnidade) {
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
		return qtdeEstoque;
	}

	public void setQtdeEstoque(Integer qtdeEstoque) {
		this.qtdeEstoque = qtdeEstoque;
	}

	public Integer getQtdeAlertaEstoque() {
		return qtdeAlertaEstoque;
	}

	public void setQtdeAlertaEstoque(Integer qtdeAlertaEstoque) {
		this.qtdeAlertaEstoque = qtdeAlertaEstoque;
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
	
	public PessoaJuridica getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(PessoaJuridica empresaId) {
		this.empresaId = empresaId;
	}	

	public Categoria getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Categoria categoriaId) {
		this.categoriaId = categoriaId;
	}	

	public Marca getMarcaId() {
		return marcaId;
	}

	public void setMarcaId(Marca marcaId) {
		this.marcaId = marcaId;
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
