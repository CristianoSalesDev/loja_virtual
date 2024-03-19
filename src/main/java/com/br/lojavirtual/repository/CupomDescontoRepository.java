package com.br.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.lojavirtual.model.CupomDesconto;

@Repository
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {

	@Query(value = "select c from CupomDesconto c where c.empresaId.id = ?1")
	public List<CupomDesconto> cupomDescontoPorEmpresa(Long idEmpresa);
	
    @Query(nativeQuery = true, value = "select cast((count(1) /5) as integer) + 1 as qtdpagina from t_cupom_desconto cd where cd.empresa_id = ?1")
    public Integer qtdPagina(Long idEmpresa);
    
    @Query("select cd from CupomDesconto cd where upper(trim(cd.codigoCupom)) like %?1% and cd.empresaId.id = ?2")
	public List<CupomDesconto> buscarCupomDescontoDes(String codigoCupom, Long empresaId);
    
	@Query("select cd from CupomDesconto cd where upper(trim(cd.codigoCupom)) like %?1%")
	List<CupomDesconto> buscarCupomDescontoDescricao(String codigoCupom);    

    @Query("select cd from CupomDesconto cd where cd.empresaId.id = ?1")
    public List<CupomDesconto> findPorPage(Long idEmpresa , Pageable pageable);    
	
}
