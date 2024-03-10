package com.br.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.br.lojavirtual.model.Acesso;
import com.br.lojavirtual.model.Categoria;
import com.br.lojavirtual.repository.AcessoRepository;
import com.br.lojavirtual.service.AcessoService;
import com.google.gson.Gson;

@Controller
@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@ResponseBody /* Pesquisa por Descrição e empresa do Acesso */
	@GetMapping(value = "**/buscarPorDescricaoAcesso/{descricao}/{empresaId}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Acesso>> buscarPorDescricaoAcesso(@PathVariable("descricao") String descricao,
			                                                           @PathVariable("empresaId") Long empresaId) { 
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDes(descricao.toUpperCase(), empresaId);
		
		return new ResponseEntity<List<Acesso>>(acesso,HttpStatus.OK);
	}	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/qtdPaginaAcesso/{idEmpresa}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa) {
		
		Integer qtdPagina = acessoRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}
	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/listaPorPageAcesso/{idEmpresa}/{pagina}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Acesso>> pageAcesso(@PathVariable("idEmpresa") Long idEmpresa,
			                                             @PathVariable("pagina") Integer pagina) {
		
		Pageable pageable = PageRequest.of(pagina, 5, Sort.by("descricao")); 
		
		List<Acesso> lista = acessoRepository.findPorPage(idEmpresa, pageable);
		
		return new ResponseEntity<List<Acesso>>(lista, HttpStatus.OK);
	}	
	
	@ResponseBody /* Poder dar um retorno da API */
	@PostMapping(value = "**/salvarAcesso") /* Mapeando a URL para receber o JSON */
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionLojaVirtual { /* Recebe o JSON e convert pra objeto */
	
		if (acesso.getId() == null) {
	  	   List<Acesso> acessos = acessoRepository.buscarAcessoDescricao(acesso.getDescricao().toUpperCase());
			  
			  if (!acessos.isEmpty()) {
				  throw new ExceptionLojaVirtual("Já existe Acesso com a descrição: " + acesso.getDescricao());
			  }
			}			
			
			Acesso acessoSalvo = acessoService.save(acesso);
			
			return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);		
	}
	
	@ResponseBody /* Poder dar um retorno da API */
	@PostMapping(value = "/deleteAcesso") /* Mapeando a URL para receber o JSON */
	public ResponseEntity<String> deleteAcesso(@RequestBody Acesso acesso) throws ExceptionLojaVirtual { /* Recebe o JSON e convert pra objeto */
		
		if (acessoRepository.findById(acesso.getId()).isPresent() == false) {
			throw new ExceptionLojaVirtual("Acesso já foi removido");
		}		
	
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity<String>(new Gson().toJson("Acesso Removido"),HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteAcessoPorId/{id}")
	public ResponseEntity<String> deleteAcessoPorId(@PathVariable("id") Long id) { 
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity<String>(new Gson().toJson("Acesso Removido"),HttpStatus.OK);
	}	
	
	@ResponseBody
	@GetMapping(value = "/obterAcesso/{id}")
	public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) throws ExceptionLojaVirtual { 
		
		Acesso acesso = acessoRepository.findById(id).orElse(null);
		
		if (acesso == null) {
			throw new ExceptionLojaVirtual("Não encontrou Acesso com código: " + id);
		}
		
		return new ResponseEntity<Acesso>(acesso,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/buscarPorDescricao/{descricao}")
	public ResponseEntity<List<Acesso>> buscarPorDescriscao(@PathVariable("descricao") String descricao) { 
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDescricao(descricao.toUpperCase());
		
		return new ResponseEntity<List<Acesso>>(acesso,HttpStatus.OK);
	}	
	
}
