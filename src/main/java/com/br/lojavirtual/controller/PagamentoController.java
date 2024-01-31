package com.br.lojavirtual.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

}
