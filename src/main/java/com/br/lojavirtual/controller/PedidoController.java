package com.br.lojavirtual.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.enums.ApiTokenIntegracao;
import com.br.lojavirtual.enums.StatusContasReceber;
import com.br.lojavirtual.model.ContasReceber;
import com.br.lojavirtual.model.Endereco;
import com.br.lojavirtual.model.ItemPedido;
import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.model.PessoaFisica;
import com.br.lojavirtual.model.StatusRastreio;
import com.br.lojavirtual.model.dto.ConsultaFreteDTO;
import com.br.lojavirtual.model.dto.EmpresaTransporteDTO;
import com.br.lojavirtual.model.dto.EnvioEtiquetaDTO;
import com.br.lojavirtual.model.dto.ItemPedidoDto;
import com.br.lojavirtual.model.dto.ObjetoPostBoletoJuno;
import com.br.lojavirtual.model.dto.PedidoDTO;
import com.br.lojavirtual.model.dto.ProductsEnvioEtiquetaDTO;
import com.br.lojavirtual.model.dto.TagsEnvioDto;
import com.br.lojavirtual.model.dto.VolumesEnvioEtiquetaDTO;
import com.br.lojavirtual.repository.ContasReceberRepository;
import com.br.lojavirtual.repository.EnderecoRepository;
import com.br.lojavirtual.repository.NfeRepository;
import com.br.lojavirtual.repository.PedidoRepository;
import com.br.lojavirtual.repository.StatusRastreioRepository;
import com.br.lojavirtual.service.PedidoService;
import com.br.lojavirtual.service.ServiceJunoBoleto;
import com.br.lojavirtual.service.ServiceSendEmail;
import com.br.lojavirtual.service.VendaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
	
	@Autowired
 	private ContasReceberRepository contasReceberRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceJunoBoleto serviceJunoBoleto;
	
	@Autowired
	private VendaService vendaService; 
	
	@ResponseBody
	@PostMapping(value = "**/salvarPedido")
	public ResponseEntity<PedidoDTO> salvarPedido(@RequestBody @Valid Pedido pedido) throws ExceptionLojaVirtual, UnsupportedEncodingException, MessagingException {
		
		
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
		
        /* Feito só para testar para usar no Postman, pois ainda não tinha o Front-End
		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistribuicao("Loja Local");
		statusRastreio.setCidade("Fortaleza");
		statusRastreio.setEmpresaId(pedido.getEmpresaId());
		statusRastreio.setEstado("CE");
		statusRastreio.setStatus("Inicio Compra");
		statusRastreio.setPedidoId(pedido);
		
		statusRastreioRepository.save(statusRastreio);
		*/
		
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
			itemVendaDTO.setProdutoId(item.getProdutoId());

			pedidosDTO.getItemPedido().add(itemVendaDTO);
		}
		
		ContasReceber contasReceber = new ContasReceber();
		contasReceber.setDescricao("Venda da loja virtual nº: " + pedido.getId());
		contasReceber.setData_pagamento(Calendar.getInstance().getTime());
		contasReceber.setData_vencimento(Calendar.getInstance().getTime());
		contasReceber.setEmpresaId(pedido.getEmpresaId());
		contasReceber.setPessoa(pedido.getPessoa());
		contasReceber.setStatusContasReceber(StatusContasReceber.LIQUIDADA);
		contasReceber.setValor_desconto(pedido.getValorDesconto());
		contasReceber.setValor_total(pedido.getValorTotal());
		
		contasReceberRepository.saveAndFlush(contasReceber);
		
		/*Email para o comprador*/
		StringBuilder msgemail = new StringBuilder();
		msgemail.append("Olá, ").append(pessoaFisica.getNome()).append("</br>");
		msgemail.append("Você realizou a compra de nº: ").append(pedido.getId()).append("</br>");
		msgemail.append("Na loja ").append(pedido.getEmpresaId().getNomeFantasia());
		
		/*assunto, msg, destino*/
		serviceSendEmail.enviarEmailHtml("Compra Realizada", msgemail.toString(), pessoaFisica.getEmail());
		
		/*Email para o vendedor*/
		msgemail = new StringBuilder();
		msgemail.append("Você realizou uma venda, nº " ).append(pedido.getId());
		serviceSendEmail.enviarEmailHtml("Venda Realizada", msgemail.toString(), pedido.getEmpresaId().getEmail());		
		
		
		return new ResponseEntity<PedidoDTO>(pedidosDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaId/{id}")
	public ResponseEntity<PedidoDTO> consultaVendaId(@PathVariable("id") Long idPedido) {

		Pedido pedido = pedidoRepository.findByIdExclusao(idPedido);
		
		if (pedido == null) {
			pedido = new Pedido();	
		}

		PedidoDTO pedidoDTO = vendaService.consultaVenda(pedido);
		/*
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
       */
		
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
				itemPedidoDtO.setProdutoId(item.getProdutoId());
	
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
				itemPedidoDTO.setProdutoId(item.getProdutoId());
	
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
				itemPedidoDTO.setProdutoId(item.getProdutoId());
	
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
				itemPedidoDTO.setProdutoId(item.getProdutoId());
	
				pedidoDTO.getItemPedido().add(itemPedidoDTO);
			}
			
			pedidoDTOList.add(pedidoDTO);
		
		}

		return new ResponseEntity<List<PedidoDTO>>(pedidoDTOList, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/cancelaEtiqueta/{idEtiqueta}/{}/{descricao}")
	public ResponseEntity<String> cancelaEtiqueta(@PathVariable String idEtiqueta, @PathVariable String reason_id, @PathVariable String descricao) throws IOException {
		
		
		OkHttpClient client = new OkHttpClient().newBuilder().build();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\"order\":{\"id\":\""+idEtiqueta+"\",\"reason_id\":\""+reason_id+"\",\"description\":\""+descricao+"\"}}");
		okhttp3.Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX+ "api/v2/me/shipment/cancel")
		  //.post(body)
		  .method("POST", body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "comprefacilnahora@gmail.com")
		  .build();

		okhttp3.Response response = client.newCall(request).execute();

		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/imprimeCompraEtiquetaFrete/{idVenda}")
	public ResponseEntity<String> imprimeCompraEtiquetaFrete(@PathVariable Long idVenda) throws ExceptionLojaVirtual, IOException {
		
		Pedido pedido = pedidoRepository.findById(idVenda).orElseGet(null);
		
		if (pedido == null) {
			return new ResponseEntity<String>("Venda não encontrada", HttpStatus.OK);
		}
		
		List<Endereco> enderecos = enderecoRepository.enderecoPj(pedido.getEmpresaId().getId());
		pedido.getEmpresaId().setEnderecos(enderecos);
		
		EnvioEtiquetaDTO envioEtiquetaDTO = new EnvioEtiquetaDTO();
		
		envioEtiquetaDTO.setService(pedido.getServicoTransportadora());
		envioEtiquetaDTO.setAgency("49");
		envioEtiquetaDTO.getFrom().setName(pedido.getEmpresaId().getNome());
		envioEtiquetaDTO.getFrom().setPhone(pedido.getEmpresaId().getTelefone());
		envioEtiquetaDTO.getFrom().setEmail(pedido.getEmpresaId().getEmail());
		envioEtiquetaDTO.getFrom().setCompany_document(pedido.getEmpresaId().getCnpj());
		envioEtiquetaDTO.getFrom().setState_register(pedido.getEmpresaId().getInscricaoEstadual());
		envioEtiquetaDTO.getFrom().setAddress(pedido.getEmpresaId().getEnderecos().get(0).getLogradouro());
		envioEtiquetaDTO.getFrom().setComplement(pedido.getEmpresaId().getEnderecos().get(0).getComplemento());
		envioEtiquetaDTO.getFrom().setNumber(pedido.getEmpresaId().getEnderecos().get(0).getNumero());
		envioEtiquetaDTO.getFrom().setDistrict(pedido.getEmpresaId().getEnderecos().get(0).getUf());
		envioEtiquetaDTO.getFrom().setCity(pedido.getEmpresaId().getEnderecos().get(0).getCidade());
		envioEtiquetaDTO.getFrom().setCountry_id(pedido.getEmpresaId().getEnderecos().get(0).getEstado());
		envioEtiquetaDTO.getFrom().setPostal_code(pedido.getEmpresaId().getEnderecos().get(0).getCep());
		envioEtiquetaDTO.getFrom().setNote("Não há");


		envioEtiquetaDTO.getTo().setName(pedido.getPessoa().getNome());
		envioEtiquetaDTO.getTo().setPhone(pedido.getPessoa().getTelefone());
		envioEtiquetaDTO.getTo().setEmail(pedido.getPessoa().getEmail());
		envioEtiquetaDTO.getTo().setDocument(pedido.getPessoa().getCpf());
		envioEtiquetaDTO.getTo().setAddress(pedido.getPessoa().enderecoEntrega().getLogradouro());
		envioEtiquetaDTO.getTo().setComplement(pedido.getPessoa().enderecoEntrega().getComplemento());
		envioEtiquetaDTO.getTo().setNumber(pedido.getPessoa().enderecoEntrega().getNumero());
		envioEtiquetaDTO.getTo().setDistrict(pedido.getPessoa().enderecoEntrega().getUf());
		envioEtiquetaDTO.getTo().setCity(pedido.getPessoa().enderecoEntrega().getCidade());
		envioEtiquetaDTO.getTo().setState_abbr(pedido.getPessoa().enderecoEntrega().getUf());
		envioEtiquetaDTO.getTo().setCountry_id(pedido.getPessoa().enderecoEntrega().getEstado());
		envioEtiquetaDTO.getTo().setPostal_code(pedido.getPessoa().enderecoEntrega().getCep());
		envioEtiquetaDTO.getTo().setNote("Não há");
		
		
		List<ProductsEnvioEtiquetaDTO> products = new ArrayList<ProductsEnvioEtiquetaDTO>();
		
		for (ItemPedido itemPedido : pedido.getItemPedidoId()) {
			
			ProductsEnvioEtiquetaDTO dto = new ProductsEnvioEtiquetaDTO();
			
			dto.setName(itemPedido.getProdutoId().getDescricao());
			dto.setQuantity(itemPedido.getQuantidade().toString());
			dto.setUnitary_value("" + itemPedido.getProdutoId().getValor().doubleValue());
			
			products.add(dto);
		}
				
		envioEtiquetaDTO.setProducts(products);
				
		List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();
		
		for (ItemPedido itemPedido : pedido.getItemPedidoId()) {
			
			VolumesEnvioEtiquetaDTO dto = new VolumesEnvioEtiquetaDTO();
			
			dto.setHeight(itemPedido.getProdutoId().getAltura().toString());
			dto.setLength(itemPedido.getProdutoId().getComprimento().toString());
			dto.setWeight(itemPedido.getProdutoId().getPeso().toString());
			dto.setWidth(itemPedido.getProdutoId().getLargura().toString());
			
			volumes.add(dto);
		}
				
		envioEtiquetaDTO.setVolumes(volumes);
		
		envioEtiquetaDTO.getOptions().setInsurance_value("" + pedido.getValorTotal().doubleValue());
		envioEtiquetaDTO.getOptions().setReceipt(false);
		envioEtiquetaDTO.getOptions().setOwn_hand(false);
		envioEtiquetaDTO.getOptions().setReverse(false);
		envioEtiquetaDTO.getOptions().setNon_commercial(false);
		envioEtiquetaDTO.getOptions().getInvoice().setKey(pedido.getNfeId().getNumero());
		envioEtiquetaDTO.getOptions().setPlatform(pedido.getEmpresaId().getNomeFantasia());
		
		TagsEnvioDto dtoTagEnvio = new TagsEnvioDto();
		dtoTagEnvio.setTag("Identificação do pedido na plataforma, exemplo:" + pedido.getId());
		dtoTagEnvio.setUrl(null);
		envioEtiquetaDTO.getOptions().getTags().add(dtoTagEnvio);
				
		String jsonEnvio = new ObjectMapper().writeValueAsString(envioEtiquetaDTO);
		
	    OkHttpClient client = new OkHttpClient().newBuilder().build();
	    okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
	     okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonEnvio);
			okhttp3.Request request = new okhttp3.Request.Builder()
			  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/cart")
			  .method("POST", body)
			  .addHeader("Accept", "application/json")
			  .addHeader("Content-Type", "application/json")
			  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
			  .addHeader("User-Agent", "comprefacilnahora@gmail.com")
			  .build();
			
			okhttp3.Response response = client.newCall(request).execute();
			
			String respostaJson = response.body().string();
			
			if (respostaJson.contains("error")) {
			   throw new ExceptionLojaVirtual(respostaJson);	
			}
		
			JsonNode jsonNode = new ObjectMapper().readTree(respostaJson);
			
			Iterator<JsonNode> iterator = jsonNode.iterator();
			
			String idEtiqueta = "";
			
			while(iterator.hasNext()) {
				JsonNode node = iterator.next();
			    if (node.get("id") != null) { 
				   idEtiqueta = node.get("id").asText();
			    }else {
			    	idEtiqueta = node.asText();
			    }
				break;
			}
			
	    /*Salvando o código da etiqueta*/
	    jdbcTemplate.execute("begin; update t_pedido set codigo_etiqueta = '"+idEtiqueta+"' where id = "+pedido.getId()+" ;commit;");	
	    //pedidoRepository.updateEtiqueta(idEtiqueta, pedido.getId());
	    
	    /* Faz a compra do frete para a etiqueta */
		OkHttpClient clientCompra = new OkHttpClient().newBuilder().build();
		 okhttp3.MediaType mediaTypeC =  okhttp3.MediaType.parse("application/json");
		 okhttp3.RequestBody bodyC =  okhttp3.RequestBody.create(mediaTypeC, "{\n    \"orders\": [\n        \""+idEtiqueta+"\"\n    ]\n}");
		 okhttp3.Request requestC = new  okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX  + "api/v2/me/shipment/checkout")
		  .method("POST", bodyC)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "comprefacilnahora@gmail.com")
		  .build();
		
		 okhttp3.Response responseC = clientCompra.newCall(requestC).execute();
		 
		 if (!responseC.isSuccessful()) {
			 return new ResponseEntity<String>("Não foi possível realizar a compra da etiqueta", HttpStatus.OK); 
		 }
		
		 
		 /* Gera as etiquetas */ 
		OkHttpClient clientGe = new OkHttpClient().newBuilder().build();
		 okhttp3.MediaType mediaTypeGe =  okhttp3.MediaType.parse("application/json");
		 okhttp3.RequestBody bodyGe =  okhttp3.RequestBody.create(mediaTypeGe, "{\n    \"orders\":[\n        \""+idEtiqueta+"\"\n    ]\n}");
		 okhttp3.Request requestGe = new  okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX  + "api/v2/me/shipment/generate")
		  .method("POST", bodyGe)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " +  ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "comprefacilnahora@gmail.com")
		  .build();
		
		 okhttp3.Response responseGe = clientGe.newCall(requestGe).execute();
		 
		 if (!responseGe.isSuccessful()) {
			 return new ResponseEntity<String>("Não foi possível gerar a etiqueta", HttpStatus.OK); 
		 }
		 
		 
			/*Faz impresão das etiquetas*/
			
			OkHttpClient clientIm = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaTypeIm = MediaType.parse("application/json");
			okhttp3.RequestBody bodyIm = okhttp3.RequestBody.create(mediaTypeIm, "{\n    \"mode\": \"private\",\n    \"orders\": [\n        \""+idEtiqueta+"\"\n    ]\n}");
					okhttp3.Request requestIm = new Request.Builder()
					  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX  + "api/v2/me/shipment/print")
					  .method("POST", bodyIm)
					  .addHeader("Accept", "application/json")
					  .addHeader("Content-Type", "application/json")
					  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
					  .addHeader("User-Agent", "comprefacilnahora@gmail.com")
					  .build();
					
					okhttp3.Response responseIm = clientIm.newCall(requestIm).execute();
					
					
			if (!responseIm.isSuccessful()) {
				 return new ResponseEntity<String>("Não foi possível imprimir a etiqueta.", HttpStatus.OK); 
			}		
					
		 String urlEtiqueta = responseIm.body().string();
			
		 jdbcTemplate.execute("begin; update t_pedido set url_imprime_etiqueta = '"+urlEtiqueta+"' where id = "+pedido.getId()+" ;commit;");
		 //pedidoRepository.updateURLEtiqueta(urlEtiqueta, pedido.getId());
		 
		    OkHttpClient clientRastreio = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaTypeR = okhttp3.MediaType.parse("application/json");
			okhttp3.RequestBody bodyR = okhttp3.RequestBody.create(mediaTypeR, "{\"orders\":[\""+idEtiqueta+"\"]}");
			okhttp3.Request requestR = new Request.Builder()
			  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX+ "api/v2/me/shipment/tracking")
			  .method("POST", bodyR)
			  //.post(body)
			  .addHeader("Accept", "application/json")
			  .addHeader("Content-type", "application/json")
			  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
			  .addHeader("User-Agent", "cristianoaragaosales@gmail.com")
			  .build();

			Response responseR = clientRastreio.newCall(requestR).execute();
			
			JsonNode jsonNodeR = new ObjectMapper().readTree(responseR.body().string());
						
			Iterator<JsonNode> iteratorR = jsonNodeR.iterator();
			
			String idEtiquetaR = "";
			
			while(iteratorR.hasNext()) {
				JsonNode node = iteratorR.next();
				 if (node.get("tracking") != null) {
				     idEtiquetaR = node.get("tracking").asText();
				 }else {
					 idEtiquetaR = node.asText(); 
				 }
				break;
			}
			
			List<StatusRastreio> rastreios = statusRastreioRepository.listaRastreioVenda(idVenda);
			 
			 if (rastreios.isEmpty()) {
				 
				 StatusRastreio rastreio = new StatusRastreio();
				 rastreio.setEmpresaId(pedido.getEmpresaId());
				 rastreio.setPedidoId(pedido);
				 rastreio.setUrlRastreio("https://www.melhorrastreio.com.br/rastreio/" + idEtiquetaR);
				 
				 statusRastreioRepository.saveAndFlush(rastreio);
			 }else {
				 statusRastreioRepository.salvaUrlRastreio("https://www.melhorrastreio.com.br/rastreio/" + idEtiquetaR, idVenda);
			 }
		
 		 return new ResponseEntity<String>("Sucesso", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@PostMapping(value = "**/gerarBoletoPix")
	public ResponseEntity<String> gerarBoletoPix(@RequestBody @Valid ObjetoPostBoletoJuno objetoPostBoletoJuno) throws Exception{
		return  new ResponseEntity<String>(serviceJunoBoleto.gerarBoletoApi(objetoPostBoletoJuno), HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "**/cancelarBoletoPix")
	public ResponseEntity<String> gerarBoletoPix(@RequestBody @Valid String code) throws Exception{
		return  new ResponseEntity<String>(serviceJunoBoleto.cancelarBoleto(code), HttpStatus.OK);
	}	
	
	@ResponseBody
	@PostMapping(value = "**/consultarFreteLojaVirtual")
	public ResponseEntity<List<EmpresaTransporteDTO>> 
	     consultaFrete(@RequestBody @Valid ConsultaFreteDTO consultaFreteDTO ) throws Exception{
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(consultaFreteDTO);
		
		OkHttpClient client = new OkHttpClient().newBuilder() .build();
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/shipment/calculate")
		  .method("POST", body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "comprefacilnahora@gmail.com")
		  .build();
		
		okhttp3.Response response = client.newCall(request).execute();
		
		JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
		
		Iterator<JsonNode> iterator = jsonNode.iterator();
		
		List<EmpresaTransporteDTO> empresaTransporteDTOs = new ArrayList<EmpresaTransporteDTO>();
		
		while(iterator.hasNext()) {
			JsonNode node = iterator.next();
			
			EmpresaTransporteDTO empresaTransporteDTO = new EmpresaTransporteDTO();
			
			if (node.get("id") != null) {
				empresaTransporteDTO.setId(node.get("id").asText());
			}
			
			if (node.get("name") != null) {
				empresaTransporteDTO.setNome(node.get("name").asText());
			}
			
			if (node.get("price") != null) {
				empresaTransporteDTO.setValor(node.get("price").asText());
			}
			
			if (node.get("company") != null) {
				empresaTransporteDTO.setEmpresa(node.get("company").get("name").asText());
				empresaTransporteDTO.setPicture(node.get("company").get("picture").asText());
			}
			
			if (empresaTransporteDTO.dadosOK()) {
				empresaTransporteDTOs.add(empresaTransporteDTO);
			}
		}
		
		return new ResponseEntity<List<EmpresaTransporteDTO>>(empresaTransporteDTOs, HttpStatus.OK);
		
	}	
}