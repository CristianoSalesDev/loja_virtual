package com.br.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.br.lojavirtual.model.PessoaJuridica;
import com.br.lojavirtual.model.Usuario;
import com.br.lojavirtual.repository.PessoaRepository;
import com.br.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica) {
		
		for (int i = 0; i< juridica.getEnderecos().size(); i++) {
			juridica.getEnderecos().get(i).setPessoa(juridica);
			juridica.getEnderecos().get(i).setEmpresaId(juridica);
		}		
	
		juridica = pessoaRepository.save(juridica);
		
	    Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());
		
		if (usuarioPj == null) {
			
			String constraint = usuarioRepository.consultaConstraintAcesso();
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table t_usuario_acesso drop constraint " + constraint +"; commit;");
			}
			
			usuarioPj = new Usuario();
			usuarioPj.setData_atual_senha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresaId(juridica);
			usuarioPj.setPessoa(juridica);
			usuarioPj.setLogin(juridica.getEmail());
			
			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPj.setSenha(senhaCript);
			
			usuarioPj = usuarioRepository.save(usuarioPj);
			
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId());
			
			/*Fazer o envio de e-mail do login e da senha*/			
			
		}
		
		return juridica;		
	}
}
