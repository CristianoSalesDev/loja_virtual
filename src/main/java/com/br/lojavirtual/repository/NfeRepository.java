package com.br.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.NFe;

@Repository
public interface NfeRepository extends JpaRepository<NFe, Long> {

}
