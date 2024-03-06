package com.br.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.Categoria;
import com.br.lojavirtual.model.dto.CategoriaDto;
import com.br.lojavirtual.repository.CategoriaRepository;

@RestController
public class CategoriaController {
	
	@Autowired
	private CategoriaRepository categoriaRepository; 
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/buscarPorDescricaoCategoria/{descricao}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Categoria>> buscarPorDescricao(@PathVariable("descricao") String descricao) { 
		
		List<Categoria> acesso = categoriaRepository.buscarCategoriaDes(descricao.toUpperCase());
		
		return new ResponseEntity<List<Categoria>>(acesso,HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@GetMapping(value = "**/listarCategoria/{codEmpresa}") /*Mapeando a url para receber JSON*/
	public ResponseEntity<List<Categoria>> listarCategoria(@PathVariable("codEmpresa") Long codEmpresa) { 
		
		List<Categoria> acesso = categoriaRepository.findAll(codEmpresa);
		
		return new ResponseEntity<List<Categoria>>(acesso,HttpStatus.OK);
	}	
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/deleteCategoria") /*Mapeando a url para receber JSON*/
	public ResponseEntity<?> deleteAcesso(@RequestBody Categoria categoria) { /*Recebe o JSON e converte pra Objeto*/
		
		if (categoriaRepository.findById(categoria.getId()).isPresent() == false) {
			return new ResponseEntity("Categoria já foi removida",HttpStatus.OK);
		}
		
		categoriaRepository.deleteById(categoria.getId());
		
		return new ResponseEntity("Categoria Removida",HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/salvarCategoria") /*Mapeando a url para receber JSON*/
	public ResponseEntity<CategoriaDto> salvarCategoria(@RequestBody Categoria categoria) throws ExceptionLojaVirtual {
		
		if (categoria.getEmpresaId() == null || (categoria.getEmpresaId().getId() == null)) {
			throw new ExceptionLojaVirtual("A empresa deve ser informada.");
		}
		
		if (categoria.getId() == null && categoriaRepository.existeCategoria(categoria.getDescricao())) {
			throw new ExceptionLojaVirtual("Não pode cadastar categoria com mesmo nome.");
		}		
		
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		CategoriaDto categoriaDto = new CategoriaDto();
		categoriaDto.setId(categoriaSalva.getId());
		categoriaDto.setDescricao(categoriaSalva.getDescricao());
		categoriaDto.setEmpresaId(categoriaSalva.getEmpresaId().getId().toString());
		
		return new ResponseEntity<CategoriaDto>(categoriaDto, HttpStatus.OK);
	}	

}
