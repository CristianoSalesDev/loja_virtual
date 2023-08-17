package com.br.lojavirtual.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.PessoaFisica;

@Repository
public interface PesssoaFisicaRepository extends CrudRepository<PessoaFisica, Long> {

}
