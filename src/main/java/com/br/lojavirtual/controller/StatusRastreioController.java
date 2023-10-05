package com.br.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.model.StatusRastreio;
import com.br.lojavirtual.repository.StatusRastreioRepository;

@RestController
public class StatusRastreioController {

	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	
	@ResponseBody
	@GetMapping(value = "**/listaRastreioVenda/{idPedido}")
	public ResponseEntity<List<StatusRastreio>> listaRastreioVenda (@PathVariable ("idPedido") Long idPedido){
		
		List<StatusRastreio> statusRastreios = statusRastreioRepository.listaRastreioVenda(idPedido);
		
		return new ResponseEntity<List<StatusRastreio>>(statusRastreios, HttpStatus.OK);
		
	}

}
