package com.br.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.br.lojavirtual.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario findUsuarioByLogin(String login);

	@Query(value = "select u from Usuario u where u.data_atual_senha <= current_date - 90")
	List<Usuario> usuarioSenhaVencida();

	@Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login =?2")
	Usuario findUserByPessoa(Long id, String email);

	@Query(value = "select constraint_name from information_schema.constraint_column_usage where table_name = 't_usuario_acesso' and column_name = 'acesso_id' and constraint_name <> 'unique_acesso_user';", nativeQuery = true)
	String consultaConstraintAcesso();

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into t_usuario_acesso(usuario_id, acesso_id) values (?1, (select id from t_acesso where descricao = 'ROLE_USER'))")
	void insereAcessoUser(Long iduser);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into t_usuario_acesso(usuario_id, acesso_id) values (?1, (select id from t_acesso where descricao = ?2 limit 1))")
	void insereAcessoUserPj(Long iduser, String acesso);

	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, value = "update t_usuario set senha = ?1 where login = ?2")	
	void updateSenhaUser(String senha, String login);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "delete from t_usuario where empresa_id = ?1")
	void deleteByPj(Long idEmpresa);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "delete from t_usuario_acesso where usuario_id in (select distinct usuario_id from t_usuario_acesso where usuario_id in (select id from t_usuario where empresa_id = ?1))")
	void deleteAcessoUserPj(Long idPj);
}
