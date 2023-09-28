package com.br.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.StatusRastreio;

@Repository
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {

}
