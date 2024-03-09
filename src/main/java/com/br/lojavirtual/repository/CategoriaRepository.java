package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	@Query(nativeQuery = true, value = "select count(1) > 0 from t_categoria where upper(trim(descricao)) = upper(trim(?1))")
	public boolean existeCategoria(String nomeCategoria);

	
	@Query("select a from Categoria a where upper(trim(a.descricao)) like %?1%")
	public List<Categoria> buscarCategoriaDes(String descricao);

    @Query("select a from Categoria a where a.empresaId.id = ?1")  
	public List<Categoria> findAll(Long codEmpresa);

    @Query("select a from Categoria a where upper(trim(a.descricao)) like %?1% and a.empresaId.id = ?2")
	public List<Categoria> buscarCategoriaDes(String descricao, Long empresaId);
    
    @Query(nativeQuery = true, value = "select cast((count(1) /5) as integer) + 1 as qtdpagina from t_categoria c where c.empresa_id = ?1")
    public Integer qtdPagina(Long idEmpresa);
    
    @Query("select a from Categoria a where a.empresaId.id = ?1")
    public List<Categoria> findPorPage(Long idEmpresa , Pageable pageable);
    
}
