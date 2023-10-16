package com.br.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.br.lojavirtual.model.Marca;
import com.br.lojavirtual.repository.CupomDescontoRepository;

@RestController
public class CupomDescontoController {

	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;
	
	@ResponseBody 
	@PostMapping(value = "**/salvarCupomDesconto") 
	public ResponseEntity<CupomDesconto> salvarCupomDesconto(@RequestBody @Valid CupomDesconto cupomDesconto) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/

		CupomDesconto cupomDescontoSalvo = cupomDescontoRepository.save(cupomDesconto);
		
		return new ResponseEntity<CupomDesconto>(cupomDescontoSalvo, HttpStatus.OK);
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
