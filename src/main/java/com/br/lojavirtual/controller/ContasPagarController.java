package com.br.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.ContasPagar;
import com.br.lojavirtual.repository.ContasPagarRepository;

@Controller
@RestController
public class ContasPagarController {

	@Autowired
	private ContasPagarRepository contasPagarRepository;
	
	@ResponseBody 
	@PostMapping(value = "**/salvarContasPagar") 
	public ResponseEntity<ContasPagar> salvarContasPagar(@RequestBody ContasPagar contasPagar) throws ExceptionLojaVirtual { 
		
		if (contasPagar.getId() == null) {
		  List<ContasPagar> contaPagars = contasPagarRepository.buscarContasPagarDescricao(contasPagar.getDescricao().toUpperCase().trim());
		  
		  if (!contaPagars.isEmpty()) {
			  throw new ExceptionLojaVirtual("Já existe Conta a Pagar com a descrição: " + contasPagar.getDescricao());
		  }
		  
		  if (contasPagar.getPessoa_fornecedor() == null || contasPagar.getPessoa_fornecedor().getId() <= 0) {
			  throw new ExceptionLojaVirtual("A pessoa responsável pela conta deve ser informada");
		  }
		  
		  if (contasPagar.getEmpresaId() == null || contasPagar.getEmpresaId().getId() <= 0) {
			  throw new ExceptionLojaVirtual("A Empresa responsável pela conta deve ser informada");
		  }
		}
		
		ContasPagar contasPagarSalvo = contasPagarRepository.save(contasPagar);
		
		return new ResponseEntity<ContasPagar>(contasPagarSalvo, HttpStatus.OK);
	}
	
	@ResponseBody 
	@PostMapping(value = "**/deleteContasPagar")
	public ResponseEntity<String> deleteContasPagar(@RequestBody ContasPagar contasPagar) { 
		
		contasPagarRepository.deleteById(contasPagar.getId());
		
		return new ResponseEntity<String>("Contas a Pagar Removida",HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteContasPagarPorId/{id}")
	public ResponseEntity<String> deleteContasPagarPorId(@PathVariable("id") Long id) { 
		
		contasPagarRepository.deleteById(id);
		
		return new ResponseEntity<String>("Contas a Pagar Removida",HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/obterContasPagar/{id}")
	public ResponseEntity<ContasPagar> obterContasPagar(@PathVariable("id") Long id) throws ExceptionLojaVirtual { 
		
		ContasPagar contasPagars = contasPagarRepository.findById(id).orElse(null);
		
		if (contasPagars == null) {
			throw new ExceptionLojaVirtual("Não encontrou Conta Pagar com código: " + id);
		}
		
		return new ResponseEntity<ContasPagar>(contasPagars,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPorContasDescricao/{descricao}")
	public ResponseEntity<List<ContasPagar>> buscarPorContaDescricao(@PathVariable("descricao") String descricao) { 
		
		List<ContasPagar> contasPagars = contasPagarRepository.buscarContasPagarDescricao(descricao.toUpperCase());
		
		return new ResponseEntity<List<ContasPagar>>(contasPagars,HttpStatus.OK);
	}	

}
