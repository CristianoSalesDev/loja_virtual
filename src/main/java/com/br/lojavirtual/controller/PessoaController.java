package com.br.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.PessoaJuridica;
import com.br.lojavirtual.repository.PessoaRepository;
import com.br.lojavirtual.service.PessoaUserService;

@RestController
public class PessoaController {
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	

	/*end-point é microsservicos é um API*/
	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtual{
		
		if (pessoaJuridica == null) {
			throw new ExceptionLojaVirtual("Pessoa juridica não pode ser NULL");
		}

		if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionLojaVirtual("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);		
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
}
