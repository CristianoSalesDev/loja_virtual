package com.br.lojavirtual.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.model.dto.PedidoDTO;
import com.br.lojavirtual.repository.PedidoRepository;
import com.br.lojavirtual.service.VendaService;

@Controller
public class PagamentoController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PedidoRepository pedidorepository;
	
	@Autowired
	private VendaService vendaService; 
	
	
	@RequestMapping(method = RequestMethod.GET, value = "**/pagamento/{idVendaCompra}")
	public ModelAndView pagamento(@PathVariable(value = "idVendaCompra", required = false) String idVendaCompra) {

		ModelAndView modelAndView = new ModelAndView("pagamento");
		
        Pedido pedido = pedidorepository.findByIdExclusao(Long.parseLong(idVendaCompra));
		
		if (pedido == null) {
			modelAndView.addObject("venda", new PedidoDTO());
		}else {
			modelAndView.addObject("venda", vendaService.consultaVenda(pedido));
		}
        
		return modelAndView;		
	     
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/finalizarCompraCartao")
	public ResponseEntity<String> finalizarCompraCartao(
			@RequestParam("cardHash") String cardHash,
			@RequestParam("cardNumber") String cardNumber,
			@RequestParam("holderName") String holderName,
			@RequestParam("securityCode") String securityCode,
			@RequestParam("expirationMonth") String expirationMonth,
			@RequestParam("expirationYear") String expirationYear,
			@RequestParam("idVendaCampo") Long idVendaCampo,
			@RequestParam("cpf") String cpf,
			@RequestParam("qtdparcela") Integer qtdparcela,
			@RequestParam("cep") String cep,
			@RequestParam("rua") String rua,
			@RequestParam("numero") String numero,
			@RequestParam("estado") String estado,
			@RequestParam("cidade") String cidade) throws Exception {
		
		System.out.println("sdd");
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
