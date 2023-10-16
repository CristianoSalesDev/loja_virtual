package com.br.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.ContasReceber;

@Repository
public interface ContasReceberRepository extends JpaRepository<ContasReceber, Long> {

}
