package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.br.lojavirtual.model.Categoria;
import com.br.lojavirtual.model.Marca;

@Repository
@Transactional
public interface MarcaRepository extends JpaRepository<Marca, Long> {

	@Query("select a from Marca a where upper(trim(a.descricao)) like %?1%")
	List<Marca> buscarMarcaDescricao(String descricao);
	
    @Query(nativeQuery = true, value = "select cast((count(1) /5) as integer) + 1 as qtdpagina from t_marca m where m.empresa_id = ?1")
    public Integer qtdPagina(Long idEmpresa);
    
    @Query("select m from Marca m where upper(trim(m.descricao)) like %?1% and m.empresaId.id = ?2")
	public List<Marca> buscarMarcaDes(String descricao, Long empresaId);
    
    @Query("select m from Marca m where m.empresaId.id = ?1")
    public List<Marca> findPorPage(Long idEmpresa , Pageable pageable);    
	
}
