package com.br.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.NFe;
import com.br.lojavirtual.repository.NfeRepository;

@RestController
public class NFeController {
	
	@Autowired
	private NfeRepository nfeRepository; 

	@ResponseBody
	@GetMapping(value = "**/consultarNFeVenda/{idPedido}")
	public ResponseEntity<List<NFe>> consultarNFeVenda(@PathVariable("idPedido") Long idPedido) throws ExceptionLojaVirtual { 
		
			List<NFe> notafiscalvenda = nfeRepository.consultarNotaFiscalPorVenda(idPedido);
		
		if (notafiscalvenda == null) {
				throw new ExceptionLojaVirtual("Não encontrou Nota Fiscal com o número do pedido: " + idPedido);
		}
		
		return new ResponseEntity<List<NFe>>(notafiscalvenda, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultarNFeVendaUnica/{idPedido}")
	public ResponseEntity<NFe> consultarNFeVendaUnica(@PathVariable("idPedido") Long idPedido) throws ExceptionLojaVirtual { 
		
			NFe notafiscalvenda = nfeRepository.consultarNotaFiscalPorVendaUnica(idPedido);
		
		if (notafiscalvenda == null) {
				throw new ExceptionLojaVirtual("Não encontrou Nota Fiscal com o número do pedido: " + idPedido);
		}
		
		return new ResponseEntity<NFe>(notafiscalvenda, HttpStatus.OK);
	}	
	
}
