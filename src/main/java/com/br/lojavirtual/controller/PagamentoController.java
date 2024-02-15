package com.br.lojavirtual.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.br.lojavirtual.enums.ApiTokenIntegracao;
import com.br.lojavirtual.model.AccessTokenJunoAPI;
import com.br.lojavirtual.model.BoletoJuno;
import com.br.lojavirtual.model.PaymentsCartaoCredito;
import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.model.PessoaFisica;
import com.br.lojavirtual.model.RetornoPagamentoCartaoJuno;
import com.br.lojavirtual.model.dto.AsaasApiPagamentoStatus;
import com.br.lojavirtual.model.dto.BoletoGeradoApiJuno;
import com.br.lojavirtual.model.dto.CartaoCreditoApiAsaas;
import com.br.lojavirtual.model.dto.CartaoCreditoAsaasHolderInfo;
import com.br.lojavirtual.model.dto.CobrancaApiAsaasCartao;
import com.br.lojavirtual.model.dto.CobrancaGeradaCartaoCreditoAsaas;
import com.br.lojavirtual.model.dto.CobrancaJunoAPI;
import com.br.lojavirtual.model.dto.ConteudoBoletoJuno;
import com.br.lojavirtual.model.dto.ErroResponseApiAsaas;
import com.br.lojavirtual.model.dto.ErroResponseApiJuno;
import com.br.lojavirtual.model.dto.ObjetoPostCarneJuno;
import com.br.lojavirtual.model.dto.PagamentoCartaoCredito;
import com.br.lojavirtual.model.dto.PedidoDTO;
import com.br.lojavirtual.repository.BoletoJunoRepository;
import com.br.lojavirtual.repository.PedidoRepository;
import com.br.lojavirtual.service.HostIgnoringCliente;
import com.br.lojavirtual.service.ServiceJunoBoleto;
import com.br.lojavirtual.service.VendaService;
import com.br.lojavirtual.util.ValidaCPF;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Controller
public class PagamentoController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private VendaService vendaService; 
	
	@Autowired
	private ServiceJunoBoleto serviceJunoBoleto;
	
	@Autowired
	private BoletoJunoRepository boletoJunoRepository; 	
	
	@RequestMapping(method = RequestMethod.GET, value = "**/pagamento/{idVendaCompra}")
	public ModelAndView pagamento(@PathVariable(value = "idVendaCompra", required = false) String idVendaCompra) {

		ModelAndView modelAndView = new ModelAndView("pagamento");
		
        Pedido pedido = pedidoRepository.findByIdExclusao(Long.parseLong(idVendaCompra));
		
		if (pedido == null) {
			modelAndView.addObject("venda", new PedidoDTO());
		}else {
			modelAndView.addObject("venda", vendaService.consultaVenda(pedido));
		}
        
		return modelAndView;		
	     
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/finalizarCompraCartao")
	public ResponseEntity<String> finalizarCompraCartaoAsaas(		
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
		
		Pedido pedido = pedidoRepository.findById(idVendaCampo).orElse(null);

		if (pedido == null) {
			   return new ResponseEntity<String>("Código da venda não existe!", HttpStatus.OK);
			}
			
			String cpfLimpo = cpf.replaceAll("\\.", "").replaceAll("\\-", "");
			
			if (!ValidaCPF.isCPF(cpfLimpo)) {
			   return new ResponseEntity<String>("CPF informado é inválido!", HttpStatus.OK);	
			}
			
			if (qtdparcela > 12 || qtdparcela <= 0) {
				   return new ResponseEntity<String>("Quantidade de parcelas deve ser de 1 até 12!", HttpStatus.OK);	
			}
			
			if (pedido.getValorTotal().doubleValue() <= 0) {
			   return new ResponseEntity<String>("Valor da Venda não pode ser Zero(0)!", HttpStatus.OK);	
			}
			
			List<BoletoJuno> cobrancas =  boletoJunoRepository.cobrancaDaVendaCompra(idVendaCampo);
			
			for (BoletoJuno boletoJuno : cobrancas) {
				boletoJunoRepository.deleteById(boletoJuno.getId());
				boletoJunoRepository.flush();
			}
			
			/*INICIO - Gerando cobranca por cartão*/
			ObjetoPostCarneJuno carne = new ObjetoPostCarneJuno();
			
			carne.setPayerCpfCnpj(cpfLimpo);
			carne.setPayerName(holderName);
			carne.setPayerPhone(pedido.getPessoa().getTelefone());			
			
			CobrancaApiAsaasCartao cobrancaApiAsaasCartao = new CobrancaApiAsaasCartao();	
			cobrancaApiAsaasCartao.setCustomer(serviceJunoBoleto.buscaClientePessoaApiAsaas(carne));
			cobrancaApiAsaasCartao.setBillingType(AsaasApiPagamentoStatus.CREDIT_CARD);
			cobrancaApiAsaasCartao.setDescription("Venda realizada para cliente por cartão de crédito: ID Venda ->" + idVendaCampo);

			if (qtdparcela == 1) {
				cobrancaApiAsaasCartao.setInstallmentValue(pedido.getValorTotal().floatValue());			
			} else {
				BigDecimal valorParcela = pedido.getValorTotal()
			            .divide(BigDecimal.valueOf(qtdparcela), RoundingMode.DOWN)
			            .setScale(2, RoundingMode.DOWN);
	
	            cobrancaApiAsaasCartao.setInstallmentValue(valorParcela.floatValue());
			}    
	            
	    		cobrancaApiAsaasCartao.setInstallmentCount(qtdparcela);
	    		cobrancaApiAsaasCartao.setDueDate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
	    		
	    		/*Dados cartão de crédito*/
	    		CartaoCreditoApiAsaas creditCard = new CartaoCreditoApiAsaas();
	    		creditCard.setCcv(securityCode);
	    		creditCard.setExpiryMonth(expirationMonth);
	    		creditCard.setExpiryYear(expirationYear);
	    		creditCard.setHolderName(holderName);
	    		creditCard.setNumber(cardNumber);
	    		
	    		cobrancaApiAsaasCartao.setCreditCard(creditCard);
	    		
	    		PessoaFisica pessoaFisica = pedido.getPessoa();
	    		CartaoCreditoAsaasHolderInfo creditCardHolderInfo = new CartaoCreditoAsaasHolderInfo();
	    		creditCardHolderInfo.setName(pessoaFisica.getNome());
	    		creditCardHolderInfo.setEmail(pessoaFisica.getEmail());
	    		creditCardHolderInfo.setCpfCnpj(pessoaFisica.getCpf());
	    		creditCardHolderInfo.setPostalCode(cep);
	    		creditCardHolderInfo.setAddressNumber(numero);
	    		creditCardHolderInfo.setAddressComplement(null);
	    		creditCardHolderInfo.setPhone(pessoaFisica.getTelefone());
	    		creditCardHolderInfo.setMobilePhone(pessoaFisica.getTelefone());
	    		
	    		cobrancaApiAsaasCartao.setCreditCardHolderInfo(creditCardHolderInfo);	
	    		
	    		String json = new ObjectMapper().writeValueAsString(cobrancaApiAsaasCartao);
	    		
	    		Client client = new HostIgnoringCliente(AsaasApiPagamentoStatus.URL_API_ASAAS).hostIgnoringCliente();
	    		WebResource webResource = client.resource(AsaasApiPagamentoStatus.URL_API_ASAAS + "payments");
	    		
	    		ClientResponse clientResponse = webResource
	    				.accept("application/json;charset=UTF-8")
	    				.header("Content-Type", "application/json;charset=UTF-8")
	    				.header("access_token", AsaasApiPagamentoStatus.API_KEY)
	    				.post(ClientResponse.class, json);
	    		
	    		String stringRetorno = clientResponse.getEntity(String.class);
	    		int status = clientResponse.getStatus();
	    		clientResponse.close();	  
	    		
	    		ObjectMapper objectMapper = new ObjectMapper();
	    		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);	    		
	    		
	    		if (status != 200) {/*Deu erro*/
	    		  	
	    			for (BoletoJuno boletoJuno : cobrancas) {
	    				
	    				if (boletoJunoRepository.existsById(boletoJuno.getId())) {
	    				   boletoJunoRepository.deleteById(boletoJuno.getId());
	    				   boletoJunoRepository.flush();
	    				}
	    			}
	    			
	    			ErroResponseApiAsaas apiAsaas = objectMapper
	    					.readValue(stringRetorno, new TypeReference<ErroResponseApiAsaas>() {});
	    			
	    			return new ResponseEntity<String>("Erro ao efetuar cobrança: " + apiAsaas.listaErros(), HttpStatus.OK);
	    		}
	    		
	    		CobrancaGeradaCartaoCreditoAsaas cartaoCredito = objectMapper.
						readValue(stringRetorno,  new TypeReference<CobrancaGeradaCartaoCreditoAsaas>() {});
	    		
	    		int recorrencia = 1;
	    		List<BoletoJuno> boletoJunos = new ArrayList<BoletoJuno>();
	    		
	    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    		Date dataCobranca = dateFormat.parse(cobrancaApiAsaasCartao.getDueDate());
	    		Calendar calendar = Calendar.getInstance();
	    		
	    		for(int p = 1 ; p <= qtdparcela; p++) {
	    			
	    			BoletoJuno boletoJuno = new BoletoJuno();

	    			boletoJuno.setChargeICartao(cartaoCredito.getId());
	    			boletoJuno.setCheckoutUrl(cartaoCredito.getInvoiceUrl());
	    			boletoJuno.setCode(cartaoCredito.getId());
	    			boletoJuno.setDataVencimento(dateFormat.format(dataCobranca));
	    			
	    			calendar.setTime(dataCobranca);
	    			calendar.add(Calendar.MONTH, 1);
	    			dataCobranca = calendar.getTime();
	    			
	    			boletoJuno.setEmpresa(pedido.getEmpresaId());
	    			boletoJuno.setIdChrBoleto(cartaoCredito.getId());
	    			boletoJuno.setIdPix(cartaoCredito.getId());
	    			boletoJuno.setInstallmentLink(cartaoCredito.getInvoiceUrl());
	    			boletoJuno.setQuitado(false);
	    			boletoJuno.setRecorrencia(recorrencia);
	    			boletoJuno.setValor(BigDecimal.valueOf(cobrancaApiAsaasCartao.getInstallmentValue()));
	    			boletoJuno.setPedidoId(pedido);
	    			
	    			recorrencia ++;
	    			boletoJunos.add(boletoJuno);
	    		}
	    		
	    		boletoJunoRepository.saveAllAndFlush(boletoJunos);
	    		
	    		if (cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {
	    			
	  			  for (BoletoJuno boletoJuno : boletoJunos) {
	  				  boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
	  			   }
	  			   
	  			  pedidoRepository.updateFinalizaVenda(pedido.getId());
	  			  
	  			  return new ResponseEntity<String>("sucesso", HttpStatus.OK);
	  		    }else {
	  			    return new ResponseEntity<String>("Pagamento não pode ser finalizado: Status:" + cartaoCredito.getStatus(), HttpStatus.OK);
	  		    }	    		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/finalizarCompraCartaoJuno")
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
		
		Pedido pedido = pedidoRepository.findById(idVendaCampo).orElse(null);
		
		if (pedido == null) {
		   return new ResponseEntity<String>("Código da venda não existe!", HttpStatus.OK);
		}
		
		String cpfLimpo = cpf.replaceAll("\\.", "").replaceAll("\\-", "");
		
		if (!ValidaCPF.isCPF(cpfLimpo)) {
		   return new ResponseEntity<String>("CPF informado é inválido!", HttpStatus.OK);	
		}
		
		if (qtdparcela > 12 || qtdparcela <= 0) {
			   return new ResponseEntity<String>("Quantidade de parcelas deve ser de 1 até 12!", HttpStatus.OK);	
		}
		
		if (pedido.getValorTotal().doubleValue() <= 0) {
		   return new ResponseEntity<String>("Valor da Venda não pode ser Zero(0)!", HttpStatus.OK);	
		}		
		
		AccessTokenJunoAPI accessTokenJunoAPI = serviceJunoBoleto.obterTokenApiJuno();
		
		if (accessTokenJunoAPI == null) {
			return new ResponseEntity<String>("Autorização bancária não foi encontrada!", HttpStatus.OK);			
		}
		
   	    CobrancaJunoAPI cobrancaJunoAPI = new CobrancaJunoAPI();
		cobrancaJunoAPI.getCharge().setPixKey(ApiTokenIntegracao.CHAVE_BOLETO_PIX);
		cobrancaJunoAPI.getCharge().setDescription("Pagamento da venda: " + pedido.getId() + " para o cliente: " + pedido.getPessoa().getNome());		
		
		 if (qtdparcela == 1) {
			 cobrancaJunoAPI.getCharge().setAmount(pedido.getValorTotal().floatValue());
		 }else {
			BigDecimal valorParcela = pedido.getValorTotal().divide(BigDecimal.valueOf(qtdparcela), RoundingMode.DOWN).setScale(2, RoundingMode.DOWN); 
			cobrancaJunoAPI.getCharge().setAmount(valorParcela.floatValue());
		 }		
		 
		 cobrancaJunoAPI.getCharge().setInstallments(qtdparcela);
		 
		 Calendar dataVencimento = Calendar.getInstance();
		 dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
		 SimpleDateFormat dateFormater = new SimpleDateFormat("yyy-MM-dd");
		 cobrancaJunoAPI.getCharge().setDueDate(dateFormater.format(dataVencimento.getTime()));
		 
		 cobrancaJunoAPI.getCharge().setFine(BigDecimal.valueOf(1.00));
		 cobrancaJunoAPI.getCharge().setInterest(BigDecimal.valueOf(1.00));
		 cobrancaJunoAPI.getCharge().setMaxOverdueDays(7);
		 cobrancaJunoAPI.getCharge().getPaymentTypes().add("CREDIT_CARD");
				 
		 cobrancaJunoAPI.getBilling().setName(holderName);
		 cobrancaJunoAPI.getBilling().setDocument(cpfLimpo);
		 cobrancaJunoAPI.getBilling().setEmail(pedido.getPessoa().getEmail());
		 cobrancaJunoAPI.getBilling().setPhone(pedido.getPessoa().getTelefone());
		 
		 /* Enviando o JSON com os dados do Front para a API de pagamento */
		 Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
		 WebResource webResource = client.resource("https://api.juno.com.br/charges");
			
		 ObjectMapper objectMapper = new ObjectMapper();
		 String json = objectMapper.writeValueAsString(cobrancaJunoAPI);
			
		 ClientResponse clientResponse = webResource
					.accept("application/json;charset=UTF-8")
					.header("Content-Type", "application/json;charset=UTF-8")
					.header("X-API-Version", 2)
					.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
					.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
					.post(ClientResponse.class, json);
			
		  String stringRetorno = clientResponse.getEntity(String.class);
		  
		  if (clientResponse.getStatus() != 200) {
			  
			  ErroResponseApiJuno jsonRetornoErro = objectMapper.
					  readValue(stringRetorno, new TypeReference<ErroResponseApiJuno>() {});
			  
			  return new ResponseEntity<String>(jsonRetornoErro.listaErro(), HttpStatus.OK);
			  
		  }
		  
		  clientResponse.close();
		  
		  objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);		
		  
		  BoletoGeradoApiJuno jsonRetorno = objectMapper.readValue(stringRetorno, new TypeReference<BoletoGeradoApiJuno>() { });
		  
		  int recorrencia = 1;
		  
		  List<BoletoJuno> boletosJuno = new ArrayList<BoletoJuno>();
		  
		  for (ConteudoBoletoJuno c : jsonRetorno.get_embedded().getCharges()) {
			  
			  BoletoJuno boletoJuno = new BoletoJuno();
			  
			  boletoJuno.setChargeICartao(c.getId());
			  boletoJuno.setCheckoutUrl(c.getCheckoutUrl());
			  boletoJuno.setCode(c.getCode());
			  boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyy-MM-dd").parse(c.getDueDate())));
			  boletoJuno.setEmpresa(pedido.getEmpresaId());
			  boletoJuno.setIdChrBoleto(c.getId());
			  boletoJuno.setIdPix(c.getPix().getId());
			  boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
			  boletoJuno.setInstallmentLink(c.getInstallmentLink());
			  boletoJuno.setLink(c.getLink());
			  boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
			  boletoJuno.setQuitado(false);
			  boletoJuno.setRecorrencia(recorrencia);
			  boletoJuno.setValor(new BigDecimal(c.getAmount()).setScale(2, RoundingMode.HALF_UP));
			  boletoJuno.setPedidoId(pedido);
			  
			  boletoJuno = boletoJunoRepository.saveAndFlush(boletoJuno);
			  
			  boletosJuno.add(boletoJuno);
			  recorrencia ++;
			  
		  }
  
		  if (boletosJuno == null || (boletosJuno != null && boletosJuno.isEmpty())) {
			  return new ResponseEntity<String>("O registro financeiro não pode ser criado para pagamento", HttpStatus.OK);
		  }
		  
		  /**------------------------REALIZANDO PAGAMENTO POR CARTÃO PARA A API-------------------------**/
		  
		  BoletoJuno boletoJunoQuitacao = boletosJuno.get(0);
		  
		  PagamentoCartaoCredito pagamentoCartaoCredito = new PagamentoCartaoCredito();
		  pagamentoCartaoCredito.setChargeId(boletoJunoQuitacao.getChargeICartao());
		  pagamentoCartaoCredito.getCreditCardDetails().setCreditCardHash(cardHash);
		  pagamentoCartaoCredito.getBilling().setEmail(pedido.getPessoa().getEmail());
		  pagamentoCartaoCredito.getBilling().getAddress().setState(estado);
		  pagamentoCartaoCredito.getBilling().getAddress().setNumber(numero);
		  pagamentoCartaoCredito.getBilling().getAddress().setCity(cidade);
		  pagamentoCartaoCredito.getBilling().getAddress().setStreet(rua);
		  pagamentoCartaoCredito.getBilling().getAddress().setPostCode(cep.replaceAll("\\-", "").replaceAll("\\.", ""));
		  
		  
			Client clientCartao = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
			WebResource webResourceCartao = clientCartao.resource("https://api.juno.com.br/payments");
			
			ObjectMapper objectMapperCartao = new ObjectMapper();
			String jsonCartao = objectMapperCartao.writeValueAsString(pagamentoCartaoCredito);
			
			System.out.println("--------Envio dados pagamento cartão-----------: "+ jsonCartao);
			
			ClientResponse clientResponseCartao = webResourceCartao
					.accept("application/json;charset=UTF-8")
					.header("Content-Type", "application/json;charset=UTF-8")
					.header("X-API-Version", 2)
					.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
					.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
					.post(ClientResponse.class, jsonCartao);
			
		  String stringRetornoCartao = clientResponseCartao.getEntity(String.class);
		  
		  System.out.println("--------Retorno dados pagamento cartão-----------: "+ stringRetornoCartao);
		  
		  if (clientResponseCartao.getStatus() != 200) {
			  
			  ErroResponseApiJuno erroResponseApiJuno = objectMapper.
					  readValue(stringRetornoCartao, new TypeReference<ErroResponseApiJuno>() {} );
			  
			    for (BoletoJuno boletoJuno : boletosJuno) {
					serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
				}
			  
			  return new ResponseEntity<String>(erroResponseApiJuno.listaErro(), HttpStatus.OK);
		  }

		  RetornoPagamentoCartaoJuno retornoPagamentoCartaoJuno = objectMapperCartao.
				  readValue(stringRetornoCartao, new TypeReference<RetornoPagamentoCartaoJuno>() { });
		  
		  if (retornoPagamentoCartaoJuno.getPayments().size() <= 0) {
			  
			  for (BoletoJuno boletoJuno : boletosJuno) {
					serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
				}
			  
			  return new ResponseEntity<String>("Nenhum pagamento foi retornado para processar.", HttpStatus.OK);
		  }
		  
		  PaymentsCartaoCredito cartaoCredito = retornoPagamentoCartaoJuno.getPayments().get(0);
		  
		  if (!cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {
			  for (BoletoJuno boletoJuno : boletosJuno) {
				serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
			}
		  }
		  
		  if (cartaoCredito.getStatus().equalsIgnoreCase("DECLINED")) {
			  return new ResponseEntity<String>("Pagamento rejeito pela análise de risco", HttpStatus.OK);
		  }else  if (cartaoCredito.getStatus().equalsIgnoreCase("FAILED")) {
			  return new ResponseEntity<String>("Pagamento não realizado por falha", HttpStatus.OK);
		  }
		  else  if (cartaoCredito.getStatus().equalsIgnoreCase("NOT_AUTHORIZED")) {
			  return new ResponseEntity<String>("Pagamento não autorizado pela instituição responsável pleo cartão de crédito, no caso, a emissora do seu cartão.", HttpStatus.OK);
		  }
		  else  if (cartaoCredito.getStatus().equalsIgnoreCase("CUSTOMER_PAID_BACK")) {
			  return new ResponseEntity<String>("Pagamento estornado a pedido do cliente.", HttpStatus.OK);
		  }
		  else  if (cartaoCredito.getStatus().equalsIgnoreCase("BANK_PAID_BACK")) {
			  return new ResponseEntity<String>("Pagamento estornado a pedido do banco.", HttpStatus.OK);
		  }
		  else  if (cartaoCredito.getStatus().equalsIgnoreCase("PARTIALLY_REFUNDED")) {
			  return new ResponseEntity<String>("Pagamento parcialmente estornado.", HttpStatus.OK);
		  }
		  else  if (cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {
			  
			   for (BoletoJuno boletoJuno : boletosJuno) {
				  boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
			   }
			   
			  pedidoRepository.updateFinalizaVenda(pedido.getId());
			  
			  return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		  }		  
		
		  return new ResponseEntity<String>("Nenhuma operação realizada!",HttpStatus.OK);
	}

}
