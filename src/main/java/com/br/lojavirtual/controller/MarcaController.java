package com.br.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.br.lojavirtual.model.Marca;
import com.br.lojavirtual.repository.MarcaRepository;

@Controller
@RestController
public class MarcaController {

	@Autowired
	private MarcaRepository marcaRepository;
	
	@ResponseBody 
	@PostMapping(value = "**/salvarMarca") 
	public ResponseEntity<Marca> salvarMarca(@RequestBody @Valid Marca marca) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/
		
		if (marca.getId() == null) {
		  List<Marca> marcas = marcaRepository.buscarMarcaDescricao(marca.getDescricao().toUpperCase());
		  
		  if (!marcas.isEmpty()) {
			  throw new ExceptionLojaVirtual("Já existe Marca com a descrição: " + marca.getDescricao());
		  }
		}
		
		Marca marcaSalvo = marcaRepository.save(marca);
		
		return new ResponseEntity<Marca>(marcaSalvo, HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/deleteMarca") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteMarca(@RequestBody Marca marca) { /*Recebe o JSON e converte pra Objeto*/
		
		marcaRepository.deleteById(marca.getId());
		
		return new ResponseEntity<String>("Marca Removida",HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteMarcaPorId/{id}")
	public ResponseEntity<String> deleteMarcaPorId(@PathVariable("id") Long id) { 
		
		marcaRepository.deleteById(id);
		
		return new ResponseEntity<String>("Marca Removida",HttpStatus.OK);
	}
	
    @ResponseBody
	@GetMapping(value = "**/obterMarcaPorId/{id}")
	public ResponseEntity<Marca> obterMarcaPorId(@PathVariable("id") Long id) throws ExceptionLojaVirtual { 
		
		Marca marca = marcaRepository.findById(id).orElse(null);
		
		if (marca == null) {
			throw new ExceptionLojaVirtual("Não encontrou a Marca com código: " + id);
		}
		
		return new ResponseEntity<Marca>(marca,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarMarcaPorDescricao/{descricao}")
	public ResponseEntity<List<Marca>> buscarMarcaPorDescricao(@PathVariable("descricao") String descricao) { 
		
		List<Marca> marca = marcaRepository.buscarMarcaDescricao(descricao.toUpperCase().trim());
		
		return new ResponseEntity<List<Marca>>(marca,HttpStatus.OK);
	}
	
}
