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

import com.br.lojavirtual.model.Acesso;
import com.br.lojavirtual.repository.AcessoRepository;
import com.br.lojavirtual.service.AcessoService;

@Controller
@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@ResponseBody /* Poder dar um retorno da API */
	@PostMapping(value = "/salvarAcesso") /* Mapeando a URL para receber o JSON */
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) { /* Recebe o JSON e convert pra objeto */
	
		Acesso acessoSalvar = acessoService.save(acesso); 
		return new ResponseEntity<Acesso>(acessoSalvar, HttpStatus.OK);		
	}
	
	@ResponseBody /* Poder dar um retorno da API */
	@PostMapping(value = "/deleteAcesso") /* Mapeando a URL para receber o JSON */
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) { /* Recebe o JSON e convert pra objeto */
	
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity("Acesso Removido",HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteAcessoPorId/{id}")
	public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) { 
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity("Acesso Removido",HttpStatus.OK);
	}	
	
	@ResponseBody
	@GetMapping(value = "/obterAcesso/{id}")
	public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) { 
		
		Acesso acesso = acessoRepository.findById(id).get();
		
		return new ResponseEntity<Acesso>(acesso,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/buscarPorDescricao/{descricao}")
	public ResponseEntity<List<Acesso>> buscarPorDescriscao(@PathVariable("descricao") String descricao) { 
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDescricao(descricao);
		
		return new ResponseEntity<List<Acesso>>(acesso,HttpStatus.OK);
	}	
	
}
