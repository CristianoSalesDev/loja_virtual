package com.br.lojavirtual.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "T_USUARIO")
@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1, initialValue = 1)
public class Usuario implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")	
	private Long id;
	
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	@Temporal(TemporalType.DATE)
	private Date data_cadastro = new Date();

	@Column(nullable = false, unique = true)
    private String login;
    
	@Column(nullable = false)
    private String senha;
    
	@Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data_atual_senha;
    
	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "pessoa_id", nullable = false, 
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private Pessoa pessoa;
	
	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "empresa_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_usu_fk"))
	private Pessoa empresaId;	

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_usuario_acesso", uniqueConstraints = @UniqueConstraint (
		       columnNames = {"usuario_id", "acesso_id"},
		       name = "unique_acesso_user"),
               joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "t_usuario", unique = false,
               foreignKey = @ForeignKey(name = "usuario_fk", value = ConstraintMode.CONSTRAINT)), 
               inverseJoinColumns = @JoinColumn(name = "acesso_id", referencedColumnName = "id", table = "t_acesso", unique = false, 
               foreignKey = @ForeignKey(name ="acesso_fk", value = ConstraintMode.CONSTRAINT)))
    private List<Acesso> acessos;
	
    /* Autoridades, são os acessos, ROLE_ADMIN, ROLE_SUPERVISOR, ROLE_USUARIO, ROLE_FINANCEIRO */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return this.acessos;
	}

	@Override
	public String getPassword() {
		
		return this.senha;
	}

	@Override
	public String getUsername() {

		return this.login;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Date getData_atual_senha() {
		return data_atual_senha;
	}

	public void setData_atual_senha(Date data_atual_senha) {
		this.data_atual_senha = data_atual_senha;
	}

	public List<Acesso> getAcessos() {
		return acessos;
	}

	public void setAcessos(List<Acesso> acessos) {
		this.acessos = acessos;
	}

	public Pessoa getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(Pessoa empresaId) {
		this.empresaId = empresaId;
	}	

}
