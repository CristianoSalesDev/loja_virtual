package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.PessoaFisica;
import com.br.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%")
	public List<PessoaJuridica> pesquisaPorNomePJ(String nome);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public List<PessoaJuridica> existeCnpjCadastradoList(String cnpj);
		
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public PessoaFisica existeCpfCadastrado(String cpf);
		
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public List<PessoaFisica> existeCpfCadastradoList(String cpf);
		
	@Query(value = "select pj from PessoaJuridica pj where pj.inscricaoEstadual = ?1")
	public PessoaJuridica existeInscricaoEstadualCadastrado(String inscEstadual);
	
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscricaoEstadual = ?1")
	public List<PessoaJuridica> existeInscricaoEstadualCadastradoList(String inscricaoEstadual);	

    @Query("select pj from PessoaJuridica pj where pj.empresaId.id = ?1")
	public List<PessoaJuridica> findPorPage(Long idEmpresa , Pageable pageable);

	@Query(nativeQuery = true, value = "select cast((count(1) /5) as integer) + 1 as qtdpagina from t_pessoa_juridica pj where pj.empresa_id = ?1")
	public Integer qtdPagina(Long idEmpresa);
	
    @Query("select pj from PessoaJuridica pj where upper(trim(pj.nomeFantasia)) like %?1% and pj.empresaId.id = ?2")
	public List<PessoaJuridica> buscarPessoaJuridicaDes(String nomeFantasia, Long empresaId);	
	
}
