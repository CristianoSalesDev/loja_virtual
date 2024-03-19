package com.br.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.CupomDesconto;
import com.br.lojavirtual.repository.CupomDescontoRepository;
import com.google.gson.Gson;

@RestController
public class CupomDescontoController {

	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/qtdPaginaCupomDesconto/{idEmpresa}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa) {
		
		Integer qtdPagina = cupomDescontoRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}	
	
	@ResponseBody /* Pesquisa por Cupom Desconto e empresa */
	@GetMapping(value = "**/buscarPorDescricaoCupomDesconto/{codigoCupom}/{empresaId}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<CupomDesconto>> buscarPorDescricaoCupomDesconto(@PathVariable("codigoCupom") String codigoCupom,
			                                                           @PathVariable("empresaId") Long empresaId) { 
		
		List<CupomDesconto> lista = cupomDescontoRepository.buscarCupomDescontoDes(codigoCupom.toUpperCase(), empresaId);
		
		return new ResponseEntity<List<CupomDesconto>>(lista,HttpStatus.OK);
	}
	
	@ResponseBody 
	@PostMapping(value = "**/salvarCupomDesconto") 
	public ResponseEntity<CupomDesconto> salvarCupomDesconto(@RequestBody @Valid CupomDesconto cupomDesconto) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/
/*
		if (cupomDesconto.getId() == null) {
			  List<CupomDesconto> cupons = cupomDescontoRepository.buscarCupomDescontoDescricao(cupomDesconto.getCodigoCupom().toUpperCase());
			  
			  if (!cupons.isEmpty()) {
				  throw new ExceptionLojaVirtual("Já existe Cupom com a descrição: " + cupomDesconto.getCodigoCupom());
			  }
			}
*/		
//		CupomDesconto cupomDescontoSalvo = cupomDescontoRepository.save(cupomDesconto);
		cupomDesconto = cupomDescontoRepository.saveAndFlush(cupomDesconto);
		
		return new ResponseEntity<CupomDesconto>(cupomDesconto, HttpStatus.OK);
	}
		
	@ResponseBody /* Deletar Cupom */
	@PostMapping(value = "**/deleteCupomDesconto") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteCupomDesconto(@RequestBody CupomDesconto cupomDesconto) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/
	
		if (cupomDescontoRepository.findById(cupomDesconto.getId()).isPresent() == false) {
			throw new ExceptionLojaVirtual("Cupom já foi removido");
		}
		
		cupomDescontoRepository.deleteById(cupomDesconto.getId());
		
		return new ResponseEntity<String>(new Gson().toJson("Cupom Removido com Sucesso!"),HttpStatus.OK);
	}

	
	@ResponseBody
	@DeleteMapping(value = "**/deleteCupomPorId/{id}")
	public ResponseEntity<String> deleteCupomPorId(@PathVariable("id") Long id) { 
		
		cupomDescontoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Cupom Removido com sucesso",HttpStatus.OK);
	}

    @ResponseBody
	@GetMapping(value = "**/obterCupomPorId/{id}")
	public ResponseEntity<CupomDesconto> obterCupomPorId(@PathVariable("id") Long id) throws ExceptionLojaVirtual { 
		
    	CupomDesconto cupomDesconto = cupomDescontoRepository.findById(id).orElse(null);
		
		if (cupomDesconto == null) {
			throw new ExceptionLojaVirtual("Não encontrou o Cupom com código: " + id);
		}
		
		return new ResponseEntity<CupomDesconto>(cupomDesconto,HttpStatus.OK);
	}
    
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/listaPorPageCupomDesconto/{idEmpresa}/{pagina}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<CupomDesconto>> pageCupomDesconto(@PathVariable("idEmpresa") Long idEmpresa,
			                                             @PathVariable("pagina") Integer pagina) {
		
		Pageable pageable = PageRequest.of(pagina, 5, Sort.by("codigoCupom")); 
		
		List<CupomDesconto> lista = cupomDescontoRepository.findPorPage(idEmpresa, pageable);
		
		return new ResponseEntity<List<CupomDesconto>>(lista, HttpStatus.OK);
	}    
	
	@ResponseBody
	@GetMapping(value = "**/listaCupomDesconto/{idEmpresa}")
	public ResponseEntity<List<CupomDesconto>> listaCupomDesconto(@PathVariable("idEmpresa") Long idEmpresa){
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.cupomDescontoPorEmpresa(idEmpresa), HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/listaCupomDesconto")
	public ResponseEntity<List<CupomDesconto>> listaCupomDesc(){
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.findAll() , HttpStatus.OK);
	}
	
}
