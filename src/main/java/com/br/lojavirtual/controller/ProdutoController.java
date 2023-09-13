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
import com.br.lojavirtual.model.Produto;
import com.br.lojavirtual.repository.ProdutoRepository;

@Controller
@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/salvarProduto") /*Mapeando a url para receber JSON*/
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/
		
		if (produto.getEmpresaId() == null || produto.getEmpresaId().getId() <= 0) {
			throw new ExceptionLojaVirtual("Fornecedor deve ser informado");
		}

		if (produto.getId() == null) {
		  List<Produto> produtos  = produtoRepository.buscarProdutoDescricao(produto.getDescricao().toUpperCase(), produto.getEmpresaId().getId());
		  
		  if (!produtos.isEmpty()) {
			  throw new ExceptionLojaVirtual("Já existe Produto com a descrição: " + produto.getDescricao());
		  }
		}
		
		if (produto.getCategoriaId() == null || produto.getCategoriaId().getId() <= 0) {
			throw new ExceptionLojaVirtual("Categoria deve ser informada");
		}		
		
		if (produto.getMarcaId() == null || produto.getMarcaId().getId() <= 0) {
			throw new ExceptionLojaVirtual("Marca deve ser informada");
		}
		
		
		Produto produtoSalvo = produtoRepository.save(produto);
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/deleteProduto") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteProduto(@RequestBody Produto produto) { /*Recebe o JSON e converte pra Objeto*/
		
		produtoRepository.deleteById(produto.getId());
		
		return new ResponseEntity<String>("Produto Removido",HttpStatus.OK);
	}
	
	//@Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })
	@ResponseBody
	@DeleteMapping(value = "**/deleteProdutoPorId/{id}")
	public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id) { 
		
		produtoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Produto Removido",HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/obterProduto/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionLojaVirtual { 
		
		Produto produto = produtoRepository.findById(id).orElse(null);
		
		if (produto == null) {
			throw new ExceptionLojaVirtual("Não encontrou Produto com código: " + id);
		}
		
		return new ResponseEntity<Produto>(produto,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarProdutoNome/{descricao}")
	public ResponseEntity<List<Produto>> buscarProdutoNome(@PathVariable("descricao") String descricao) { 
		
		List<Produto> acesso = produtoRepository.buscarProdutoDescricao(descricao.toUpperCase());
		
		return new ResponseEntity<List<Produto>>(acesso,HttpStatus.OK);
	}
	
}
