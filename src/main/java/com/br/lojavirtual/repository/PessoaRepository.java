package com.br.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.PessoaFisica;
import com.br.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public PessoaFisica existeCpfCadastrado(String cpf);	
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscricaoEstadual = ?1")
	public PessoaJuridica existeinscricaoEstadualCadastrado(String inscricaoEstadual);	

}
