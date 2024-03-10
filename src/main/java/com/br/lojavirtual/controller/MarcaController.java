package com.br.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.br.lojavirtual.model.Categoria;
import com.br.lojavirtual.model.Marca;
import com.br.lojavirtual.repository.MarcaRepository;
import com.google.gson.Gson;

@Controller
@RestController
public class MarcaController {

	@Autowired
	private MarcaRepository marcaRepository;
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/listaPorPageMarca/{idEmpresa}/{pagina}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Marca>> pageMarca(@PathVariable("idEmpresa") Long idEmpresa,
			                                             @PathVariable("pagina") Integer pagina) {
		
		Pageable pageable = PageRequest.of(pagina, 5, Sort.by("descricao")); 
		
		List<Marca> lista = marcaRepository.findPorPage(idEmpresa, pageable);
		
		return new ResponseEntity<List<Marca>>(lista, HttpStatus.OK);
	}	
	
	@ResponseBody /* Pesquisa por Descrição e empresa da Marca */
	@GetMapping(value = "**/buscarPorDescricaoMarca/{descricao}/{empresaId}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Marca>> buscarPorDescricaoMarca(@PathVariable("descricao") String descricao,
			                                                           @PathVariable("empresaId") Long empresaId) { 
		
		List<Marca> lista = marcaRepository.buscarMarcaDes(descricao.toUpperCase(), empresaId);
		
		return new ResponseEntity<List<Marca>>(lista,HttpStatus.OK);
	}	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/qtdPaginaMarca/{idEmpresa}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa) {
		
		Integer qtdPagina = marcaRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}	
	
	@ResponseBody 
	@PostMapping(value = "**/salvarMarca") /*Mapeando a url para receber JSON*/ 
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
	
	@ResponseBody /* Deletar Marca */
	@PostMapping(value = "**/deleteMarca") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteMarca(@RequestBody Marca marca) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/
	
		if (marcaRepository.findById(marca.getId()).isPresent() == false) {
			throw new ExceptionLojaVirtual("Marca já foi removida");
		}
		
		marcaRepository.deleteById(marca.getId());
		
		return new ResponseEntity<String>(new Gson().toJson("Marca Removida"),HttpStatus.OK);
	}
	
	@ResponseBody /* Deletar Marca por ID */
	@DeleteMapping(value = "**/deleteMarcaPorId/{id}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteMarcaPorId(@PathVariable("id") Long id) { 
		
		marcaRepository.deleteById(id);
		
		return new ResponseEntity<String>("Marca Removida",HttpStatus.OK);
	}
	
    @ResponseBody /* Pesquisar Marca por ID */
	@GetMapping(value = "**/obterMarcaPorId/{id}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<Marca> obterMarcaPorId(@PathVariable("id") Long id) throws ExceptionLojaVirtual { 
		
		Marca marca = marcaRepository.findById(id).orElse(null);
		
		if (marca == null) {
			throw new ExceptionLojaVirtual("Não encontrou a Marca com código: " + id);
		}
		
		return new ResponseEntity<Marca>(marca,HttpStatus.OK);
	}
	
	@ResponseBody /* Deletar Marca por Descrição */
	@GetMapping(value = "**/buscarMarcaPorDescricao/{descricao}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Marca>> buscarMarcaPorDescricao(@PathVariable("descricao") String descricao) { 
		
		List<Marca> marca = marcaRepository.buscarMarcaDescricao(descricao.toUpperCase().trim());
		
		return new ResponseEntity<List<Marca>>(marca,HttpStatus.OK);
	}
	
}
