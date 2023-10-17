package com.br.lojavirtual.controller;

import java.util.ArrayList;
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
import com.br.lojavirtual.model.NotaEntrada;
import com.br.lojavirtual.model.dto.ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO;
import com.br.lojavirtual.model.dto.ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO;
import com.br.lojavirtual.model.dto.ObjetoRelatorioStatusCompra;
import com.br.lojavirtual.repository.NotaEntradaRepository;
import com.br.lojavirtual.service.NotaFiscalEntradaService;

@RestController
public class NotaEntradaController {

	@Autowired
	private NotaEntradaRepository notaEntradaRepository;
	
    @Autowired
	private NotaFiscalEntradaService notaFiscalEntradaService;
    
    @ResponseBody
	@PostMapping(value = "**/relatorioStatusCompra")
	public ResponseEntity<List<ObjetoRelatorioStatusCompra>> relatorioStatusCompra (@Valid 
			             @RequestBody  ObjetoRelatorioStatusCompra objetoRelatorioStatusCompra){
		
		List<ObjetoRelatorioStatusCompra> retorno = new ArrayList<ObjetoRelatorioStatusCompra>();
		
		retorno = notaFiscalEntradaService.relatorioStatusVendaLojaVirtual(objetoRelatorioStatusCompra);
		
		return new ResponseEntity<List<ObjetoRelatorioStatusCompra>>(retorno, HttpStatus.OK);
		
	}    
	
	@ResponseBody
	@PostMapping(value = "**/relatorioProdutosCompradoNotaFiscal")
	public ResponseEntity<List<ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO>> relatorioProdutosCompradoNotaFiscal
	    (@Valid @RequestBody ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto){
		
		List<ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO> retorno = 
				new ArrayList<ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO>();
		
		retorno = notaFiscalEntradaService.gerarRelatorioProdutosCompraNota(obejtoRequisicaoRelatorioProdutosCompraNotaFiscalDto);
				
		return new ResponseEntity<List<ObejtoRequisicaoRelatorioProdutosCompraNotaFiscalDTO>>(retorno, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@PostMapping(value = "**/relatorioProdutosAlertaEstoque")
	public ResponseEntity<List<ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO>> relatorioProdutosAlertaEstoque
	    (@Valid @RequestBody ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO obejtoRequisicaoRelatorioProdutosAlertaEstoqueDto ){
		
		List<ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO> retorno = 
				new ArrayList<ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO>();
		
		retorno = notaFiscalEntradaService.gerarRelatorioAlertaEstoque(obejtoRequisicaoRelatorioProdutosAlertaEstoqueDto);
		
		
		return new ResponseEntity<List<ObejtoRequisicaoRelatorioProdutosAlertaEstoqueDTO>>(retorno, HttpStatus.OK);
		
	}
	
	@ResponseBody 
	@PostMapping(value = "**/salvarNotaEntrada")
	public ResponseEntity<NotaEntrada> salvarNotaEntrada(@RequestBody @Valid NotaEntrada notaEntrada) throws ExceptionLojaVirtual { /*Recebe o JSON e converte pra Objeto*/
		
		if (notaEntrada.getId() == null) {
		  
			if (notaEntrada.getObservacao() != null) {
				boolean existe = notaEntradaRepository.existeNotaComObservacao(notaEntrada.getObservacao().toUpperCase().trim());
			   
				if(existe) {
				   throw new ExceptionLojaVirtual("Já existe Nota de compra com essa mesma observação : " + notaEntrada.getObservacao());
			   }
			}
		}
		
		if (notaEntrada.getPessoa() == null || notaEntrada.getPessoa().getId() <= 0) {
			throw new ExceptionLojaVirtual("A Pessoa Juridica da nota fiscal deve ser informada.");
		}
		
		if (notaEntrada.getEmpresaId() == null || notaEntrada.getEmpresaId().getId() <= 0) {
			throw new ExceptionLojaVirtual("A empresa responsável deve ser informada.");
		}
		
		if (notaEntrada.getContasPagarId() == null || notaEntrada.getContasPagarId().getId() <= 0) {
			throw new ExceptionLojaVirtual("A conta a pagar da nota deve ser informada.");
		}
		
		NotaEntrada notaEntradaSalva = notaEntradaRepository.save(notaEntrada);
		
		return new ResponseEntity<NotaEntrada>(notaEntradaSalva, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteNotaEntradaPorId/{id}")
	public ResponseEntity<?> deleteNotaEntradaPorId(@PathVariable("id") Long id) { 
		
		
		notaEntradaRepository.deleteItemNotaEntrada(id);/*Delete os filhos*/
		notaEntradaRepository.deleteById(id); /*Deleta o pai*/
		
		return new ResponseEntity("Nota Fiscal de Entrada Removida",HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/obterNotaEntrada/{id}")
	public ResponseEntity<NotaEntrada> obterNotaEntrada(@PathVariable("id") Long id) throws ExceptionLojaVirtual { 
		
		NotaEntrada notaEntrada = notaEntradaRepository.findById(id).orElse(null);
		
		if (notaEntrada == null) {
			throw new ExceptionLojaVirtual("Não encontrou Nota Fiscal com código: " + id);
		}
		
		return new ResponseEntity<NotaEntrada>(notaEntrada, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarNotaEntradaPorObservacao/{obs}")
	public ResponseEntity<List<NotaEntrada>> buscarNotaEntradaPorObservacao(@PathVariable("obs") String obs) { 
		
		List<NotaEntrada> notaEntradas = notaEntradaRepository.buscaObservacaoNota(obs.toUpperCase().trim());
		
		return new ResponseEntity<List<NotaEntrada>>(notaEntradas,HttpStatus.OK);
	}	
	
}
