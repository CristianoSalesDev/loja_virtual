package com.br.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.Endereco;
import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.model.PessoaFisica;
import com.br.lojavirtual.model.dto.PedidoDTO;
import com.br.lojavirtual.repository.EnderecoRepository;
import com.br.lojavirtual.repository.NfeRepository;
import com.br.lojavirtual.repository.PedidoRepository;

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
		
		/*Salva primeiro a venda e todo os dados*/
		pedido = pedidoRepository.saveAndFlush(pedido);
		
		/*Associa a venda gravada no banco com a nota fiscal */
		pedido.getNfeId().setPedidoId(pedido);
	
		/*Persiste novamente as nota fiscal novamente pra ficar amarrada na venda */
		nfeRepository.saveAndFlush(pedido.getNfeId());
		
		PedidoDTO pedidosDTO = new PedidoDTO();
		pedidosDTO.setValorTotal(pedido.getValorTotal());
		pedidosDTO.setPessoa(pedido.getPessoa());
		
		return new ResponseEntity<PedidoDTO>(pedidosDTO, HttpStatus.OK);
	}	
	
}