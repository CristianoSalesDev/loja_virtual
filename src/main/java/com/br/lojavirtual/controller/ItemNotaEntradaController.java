package com.br.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.ItemEntrada;
import com.br.lojavirtual.repository.ItemNotaEntradaRepository;

@RestController
public class ItemNotaEntradaController {

	@Autowired
	private ItemNotaEntradaRepository itemNotaEntradaRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarItemEntrada")
	public ResponseEntity<ItemEntrada>salvarItemEntrada(@RequestBody @Valid ItemEntrada itemEntrada) throws ExceptionLojaVirtual {
		
		if (itemEntrada.getId() == null) {
			
			if (itemEntrada.getProduto_id() == null || itemEntrada.getProduto_id().getId() <= 0) {
				throw new ExceptionLojaVirtual("O produto deve ser informado.");
			}
			
			
			if (itemEntrada.getNotaEntradaId() == null || itemEntrada.getNotaEntradaId().getId() <= 0) {
				throw new ExceptionLojaVirtual("A nota fisca deve ser informada.");
			}
			
			
			if (itemEntrada.getEmpresaId() == null || itemEntrada.getEmpresaId().getId() <= 0) {
				throw new ExceptionLojaVirtual("A empresa deve ser informada.");
			}
			
			List<ItemEntrada> notaExistente = itemNotaEntradaRepository.buscaItemEntradaPorProdutoNota(itemEntrada.getProduto_id().getId(),itemEntrada.getNotaEntradaId().getId());
			
			if (!notaExistente.isEmpty()) {
				throw new ExceptionLojaVirtual("Já existe este produto cadastrado para esta nota.");
			}
			
		}
		
		if (itemEntrada.getQuantidade() <=0) {
			throw new ExceptionLojaVirtual("A quantidade do produto deve ser informada.");
		}
		
		ItemEntrada itemEntradaSalva = itemNotaEntradaRepository.save(itemEntrada);
		
		itemEntradaSalva = itemNotaEntradaRepository.findById(itemEntrada.getId()).get();
		
		return new ResponseEntity<ItemEntrada>(itemEntradaSalva, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteItemEntradaPorId/{id}")
	public ResponseEntity<?> deleteItemEntradaPorId(@PathVariable("id") Long id) { 
		
		
		itemNotaEntradaRepository.deleteByIdItemEntrada(id);
		
		return new ResponseEntity("Nota Item Produto Removido",HttpStatus.OK);
	}	
	
}
