package com.br.lojavirtual.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.Endereco;
import com.br.lojavirtual.model.ItemPedido;
import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.model.PessoaFisica;
import com.br.lojavirtual.model.StatusRastreio;
import com.br.lojavirtual.model.dto.ItemPedidoDto;
import com.br.lojavirtual.model.dto.PedidoDTO;
import com.br.lojavirtual.repository.EnderecoRepository;
import com.br.lojavirtual.repository.NfeRepository;
import com.br.lojavirtual.repository.PedidoRepository;
import com.br.lojavirtual.repository.StatusRastreioRepository;
import com.br.lojavirtual.service.PedidoService;

@RestController
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private NfeRepository nfeRepository;
	
	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private PedidoService pedidoService;
	
	@ResponseBody
	@PostMapping(value = "**/salvarPedido")
	public ResponseEntity<PedidoDTO> salvarPedido(@RequestBody @Valid Pedido pedido) throws ExceptionLojaVirtual {
		
		
		pedido.getPessoa().setEmpresaId(pedido.getEmpresaId());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(pedido.getPessoa()).getBody();
		pedido.setPessoa(pessoaFisica);
		
		pedido.getEnderecoCobranca().setPessoa(pessoaFisica);
		pedido.getEnderecoCobranca().setEmpresaId(pedido.getEmpresaId());
		Endereco enderecoCobranca = enderecoRepository.save(pedido.getEnderecoCobranca());
		pedido.setEnderecoCobranca(enderecoCobranca);
		
		pedido.getEnderecoEntrega().setPessoa(pessoaFisica);
		pedido.getEnderecoEntrega().setEmpresaId(pedido.getEmpresaId());
		Endereco enderecoEntrega = enderecoRepository.save(pedido.getEnderecoEntrega());
		pedido.setEnderecoEntrega(enderecoEntrega);

		pedido.getNfeId().setEmpresaId(pedido.getEmpresaId());		
		
		for (int i = 0; i < pedido.getItemPedidoId().size(); i++) {
			pedido.getItemPedidoId().get(i).setEmpresaId(pedido.getEmpresaId());
			pedido.getItemPedidoId().get(i).setPedidoId(pedido);			
		}
		
		/*Salva primeiro a venda e todo os dados*/
		pedido = pedidoRepository.saveAndFlush(pedido);
		
		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistribuicao("Loja Local");
		statusRastreio.setCidade("Fortaleza");
		statusRastreio.setEmpresaId(pedido.getEmpresaId());
		statusRastreio.setEstado("CE");
		statusRastreio.setStatus("Inicio Compra");
		statusRastreio.setPedidoId(pedido);
		
		statusRastreioRepository.save(statusRastreio);
		
		/*Associa a venda gravada no banco com a nota fiscal */
		pedido.getNfeId().setPedidoId(pedido);
	
		/*Persiste novamente as nota fiscal novamente pra ficar amarrada na venda */
		nfeRepository.saveAndFlush(pedido.getNfeId());
		
		PedidoDTO pedidosDTO = new PedidoDTO();
		pedidosDTO.setValorTotal(pedido.getValorTotal());
		pedidosDTO.setPessoa(pedido.getPessoa());
		
		pedidosDTO.setEntrega(pedido.getEnderecoEntrega());
		pedidosDTO.setCobranca(pedido.getEnderecoCobranca());

		pedidosDTO.setValorDesconto(pedido.getValorDesconto());
		pedidosDTO.setValorFrete(pedido.getValorFrete());
		pedidosDTO.setId(pedido.getId());
		
		for (ItemPedido item : pedido.getItemPedidoId()) {

			ItemPedidoDto itemVendaDTO = new ItemPedidoDto();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProdutoId());

			pedidosDTO.getItemPedido().add(itemVendaDTO);
		}
		
		return new ResponseEntity<PedidoDTO>(pedidosDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaId/{id}")
	public ResponseEntity<PedidoDTO> consultaVendaId(@PathVariable("id") Long idPedido) {

		Pedido pedido = pedidoRepository.findByIdExclusao(idPedido);
		
		if (pedido == null) {
			pedido = new Pedido();	
		}

		PedidoDTO pedidoDTO = new PedidoDTO();

		pedidoDTO.setValorTotal(pedido.getValorTotal());
		pedidoDTO.setPessoa(pedido.getPessoa());

		pedidoDTO.setEntrega(pedido.getEnderecoEntrega());
		pedidoDTO.setCobranca(pedido.getEnderecoCobranca());

		pedidoDTO.setValorDesconto(pedido.getValorDesconto());
		pedidoDTO.setValorFrete(pedido.getValorFrete());
		pedidoDTO.setId(pedido.getId());

		for (ItemPedido item : pedido.getItemPedidoId()) {

			ItemPedidoDto itemVendaDTO = new ItemPedidoDto();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProdutoId());

			pedidoDTO.getItemPedido().add(itemVendaDTO);
		}

		return new ResponseEntity<PedidoDTO>(pedidoDTO, HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deletePedido/{idPedido}")
	public ResponseEntity<String> deletePedido(@PathVariable(value = "idPedido") Long idPedido) {
		
		pedidoService.exclusaoPedido(idPedido);
		
		return new ResponseEntity<String>("Venda excluída com sucesso.",HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletePedidoExcluido/{idPedido}")
	public ResponseEntity<String> deletePedidoExcluido(@PathVariable(value = "idPedido") Long idPedido) {
		
		pedidoService.exclusaoPedidoExcluido(idPedido);
		
		return new ResponseEntity<String>("Venda excluída logicamente com sucesso.",HttpStatus.OK);
		
	}	
	
	@ResponseBody
	@PutMapping(value = "**/ativaPedidoExcluido/{idPedido}")
	public ResponseEntity<String> ativaPedidoExcluido(@PathVariable(value = "idPedido") Long idPedido) {
		
		pedidoService.ativarPedidoExcluido(idPedido);
		
		return new ResponseEntity<String>("Venda ativada logicamente com sucesso.",HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaDinamicaPorData/{data1}/{data2}")
	public ResponseEntity<List<PedidoDTO>> 
	                            consultaVendaDinamicaPorData(
	                            		@PathVariable("data1") String data1,
	                            		@PathVariable("data2") String data2) throws ParseException{
		
		List<Pedido> pedido = null;
		
		pedido = pedidoService.consultaVendaPorData(data1, data2);
		
		if (pedido == null) {
			pedido = new ArrayList<Pedido>();
		}
		
        List<PedidoDTO> pedidoDTOList = new ArrayList<PedidoDTO>();
		
		for (Pedido ped : pedido) {
			
			PedidoDTO pedidoDTO = new PedidoDTO();
	
			pedidoDTO.setValorTotal(ped.getValorTotal());
			pedidoDTO.setPessoa(ped.getPessoa());
	
			pedidoDTO.setEntrega(ped.getEnderecoEntrega());
			pedidoDTO.setCobranca(ped.getEnderecoCobranca());
	
			pedidoDTO.setValorDesconto(ped.getValorDesconto());
			pedidoDTO.setValorFrete(ped.getValorFrete());
			pedidoDTO.setId(ped.getId());

			for (ItemPedido item : ped.getItemPedidoId()) {
	
				ItemPedidoDto itemPedidoDtO = new ItemPedidoDto();
				itemPedidoDtO.setQuantidade(item.getQuantidade());
				itemPedidoDtO.setProduto(item.getProdutoId());
	
				pedidoDTO.getItemPedido().add(itemPedidoDtO);
			}
			
			pedidoDTOList.add(pedidoDTO);
		
		}

		return new ResponseEntity<List<PedidoDTO>>(pedidoDTOList, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaDinamica/{valor}/{tipoconsulta}")
	public ResponseEntity<List<PedidoDTO>> consultaVendaDinamica(@PathVariable("valor") String valor,
	                                                             @PathVariable("tipoconsulta") String tipoconsulta) {

		List<Pedido> pedido = null;
		
		if (tipoconsulta.equalsIgnoreCase("POR_ID_PRODUTO")) {
			
			pedido =  pedidoRepository.consultaPorProduto(Long.parseLong(valor));
			
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_PRODUTO")) {
			pedido = pedidoRepository.consultaPorNomeProduto(valor.toUpperCase().trim());
		}
		else if (tipoconsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
			pedido = pedidoRepository.consultaPorNomeCliente(valor.toUpperCase().trim());
		}
		else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_COBRANCA")) {
			pedido = pedidoRepository.consultaPorEnderecoCobranca(valor.toUpperCase().trim());
		}
		else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_ENTREGA")) {
			pedido = pedidoRepository.consultaPorEnderecoEntrega(valor.toUpperCase().trim());
		}		
		
		if (pedido == null) {
			pedido = new ArrayList<Pedido>();
		}
		
		List<PedidoDTO> pedidoDTOList = new ArrayList<PedidoDTO>();
		
		for (Pedido ped : pedido) {
			
			PedidoDTO pedidoDTO = new PedidoDTO();
	
			pedidoDTO.setValorTotal(ped.getValorTotal());
			pedidoDTO.setPessoa(ped.getPessoa());
	
			pedidoDTO.setEntrega(ped.getEnderecoEntrega());
			pedidoDTO.setCobranca(ped.getEnderecoCobranca());
	
			pedidoDTO.setValorDesconto(ped.getValorDesconto());
			pedidoDTO.setValorFrete(ped.getValorFrete());
			pedidoDTO.setId(ped.getId());

			for (ItemPedido item : ped.getItemPedidoId()) {
	
				ItemPedidoDto itemPedidoDTO = new ItemPedidoDto();
				itemPedidoDTO.setQuantidade(item.getQuantidade());
				itemPedidoDTO.setProduto(item.getProdutoId());
	
				pedidoDTO.getItemPedido().add(itemPedidoDTO);
			}
			
			pedidoDTOList.add(pedidoDTO);
		
		}

		return new ResponseEntity<List<PedidoDTO>>(pedidoDTOList, HttpStatus.OK);
	}	
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaPorProdutoId/{id}")
	public ResponseEntity<List<PedidoDTO>> consultaVendaPorProduto(@PathVariable("id") Long idProduto) {

		List<Pedido> pedido = pedidoRepository.consultaPorProduto(idProduto);
		
		if (pedido == null) {
			pedido = new ArrayList<Pedido>();
		}
		
		List<PedidoDTO> pedidoDTOList = new ArrayList<PedidoDTO>();
		
		for (Pedido ped : pedido) {
			
			PedidoDTO pedidoDTO = new PedidoDTO();
	
			pedidoDTO.setValorTotal(ped.getValorTotal());
			pedidoDTO.setPessoa(ped.getPessoa());
	
			pedidoDTO.setEntrega(ped.getEnderecoEntrega());
			pedidoDTO.setCobranca(ped.getEnderecoCobranca());
	
			pedidoDTO.setValorDesconto(ped.getValorDesconto());
			pedidoDTO.setValorFrete(ped.getValorFrete());
			pedidoDTO.setId(ped.getId());

			for (ItemPedido item : ped.getItemPedidoId()) {
	
				ItemPedidoDto itemPedidoDTO = new ItemPedidoDto();
				itemPedidoDTO.setQuantidade(item.getQuantidade());
				itemPedidoDTO.setProduto(item.getProdutoId());
	
				pedidoDTO.getItemPedido().add(itemPedidoDTO);
			}
			
			pedidoDTOList.add(pedidoDTO);
		
		}

		return new ResponseEntity<List<PedidoDTO>>(pedidoDTOList, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaPorClienteId/{idCliente}")
	public ResponseEntity<List<PedidoDTO>> consultaPorClienteId(@PathVariable("idCliente") Long idCliente) {

		List<Pedido> pedido = pedidoRepository.consultaPorIdCliente(idCliente);
		
		if (pedido == null) {
			pedido = new ArrayList<Pedido>();
		}
		
		List<PedidoDTO> pedidoDTOList = new ArrayList<PedidoDTO>();
		
		for (Pedido ped : pedido) {
			
			PedidoDTO pedidoDTO = new PedidoDTO();
	
			pedidoDTO.setValorTotal(ped.getValorTotal());
			pedidoDTO.setPessoa(ped.getPessoa());
	
			pedidoDTO.setEntrega(ped.getEnderecoEntrega());
			pedidoDTO.setCobranca(ped.getEnderecoCobranca());
	
			pedidoDTO.setValorDesconto(ped.getValorDesconto());
			pedidoDTO.setValorFrete(ped.getValorFrete());
			pedidoDTO.setId(ped.getId());

			for (ItemPedido item : ped.getItemPedidoId()) {
	
				ItemPedidoDto itemPedidoDTO = new ItemPedidoDto();
				itemPedidoDTO.setQuantidade(item.getQuantidade());
				itemPedidoDTO.setProduto(item.getProdutoId());
	
				pedidoDTO.getItemPedido().add(itemPedidoDTO);
			}
			
			pedidoDTOList.add(pedidoDTO);
		
		}

		return new ResponseEntity<List<PedidoDTO>>(pedidoDTOList, HttpStatus.OK);
	}	
}