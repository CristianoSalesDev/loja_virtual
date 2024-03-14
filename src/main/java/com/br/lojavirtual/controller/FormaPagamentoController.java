package com.br.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.FormaPagamento;
import com.br.lojavirtual.repository.FormaPagamentoRepository;
import com.google.gson.Gson;

@RestController
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@ResponseBody 
	@PostMapping(value = "**/salvarFormaPagamento") 
	public ResponseEntity<FormaPagamento> salvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) 
			throws ExceptionLojaVirtual { 

		formaPagamento = formaPagamentoRepository.saveAndFlush(formaPagamento);
		
		return new ResponseEntity<FormaPagamento>(formaPagamento, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/listaFormaPagamento/{idEmpresa}")
	public ResponseEntity<List<FormaPagamento>> listaFormaPagamentoidEmpresa(@PathVariable(value = "idEmpresa") Long idEmpresa){
		
		return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(idEmpresa), HttpStatus.OK);
		
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/deleteFormaPagamento") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteFP(@RequestBody FormaPagamento formaPagamento) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/
		
		if (formaPagamentoRepository.findById(formaPagamento.getId()).isPresent() == false) {
			throw new ExceptionLojaVirtual("Forma de Pagamento já foi removida");
		}
		
		formaPagamentoRepository.deleteById(formaPagamento.getId());
		
		return new ResponseEntity<String>(new Gson().toJson("Forma Pagamento Removida com sucesso"),HttpStatus.OK);
	}	
	
	@ResponseBody
	@GetMapping(value = "**/listaFormaPagamento")
	public ResponseEntity<List<FormaPagamento>> listaFormaPagamento(){
		
		return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(), HttpStatus.OK);
		
	}

	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/qtdPaginaFormaPagamento/{idEmpresa}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa) {
		
		Integer qtdPagina = formaPagamentoRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/buscarPorIdFp/{id}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<FormaPagamento> buscarPorIdFp(@PathVariable("id") Long id) { 
		
		FormaPagamento formaPagamentoId = formaPagamentoRepository.findById(id).get();
		
		return new ResponseEntity<FormaPagamento>(formaPagamentoId,HttpStatus.OK);
	}
	
	@ResponseBody /* Pesquisa por Descrição e empresa da Forma de Pagamento */
	@GetMapping(value = "**/buscarPorDescricaoFormaPagamento/{descricao}/{empresaId}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<FormaPagamento>> buscarPorDescricao2(@PathVariable("descricao") String descricao,
			                                                           @PathVariable("empresaId") Long empresaId) { 
		
		List<FormaPagamento> fp = formaPagamentoRepository.buscarFormaPagamentoDes(descricao.toUpperCase(), empresaId);
		
		return new ResponseEntity<List<FormaPagamento>>(fp,HttpStatus.OK);
	}	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/listaPorPageFormaPagamento/{idEmpresa}/{pagina}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<FormaPagamento>> pageFormaPagamento(@PathVariable("idEmpresa") Long idEmpresa,
			                                             @PathVariable("pagina") Integer pagina) {
		
		Pageable pageable = PageRequest.of(pagina, 5, Sort.by("descricao")); 
		
		List<FormaPagamento> lista = formaPagamentoRepository.findPorPage(idEmpresa, pageable);
		
		return new ResponseEntity<List<FormaPagamento>>(lista, HttpStatus.OK);
	}
	
}
