package com.br.lojavirtual.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.lojavirtual.enums.ApiTokenIntegracao;
import com.br.lojavirtual.model.AccessTokenJunoAPI;
import com.br.lojavirtual.model.BoletoJuno;
import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.model.dto.BoletoGeradoApiJuno;
import com.br.lojavirtual.model.dto.CobrancaJunoAPI;
import com.br.lojavirtual.model.dto.ConteudoBoletoJuno;
import com.br.lojavirtual.model.dto.CriarWebHook;
import com.br.lojavirtual.model.dto.ObjetoPostBoletoJuno;
import com.br.lojavirtual.repository.AccesTokenJunoRepository;
import com.br.lojavirtual.repository.BoletoJunoRepository;
import com.br.lojavirtual.repository.PedidoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class ServiceJunoBoleto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AccessTokenJunoService accessTokenJunoService;
	
	@Autowired
	private AccesTokenJunoRepository accesTokenJunoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private BoletoJunoRepository boletoJunoRepository;
	
	
	public String cancelarBoleto(String code) throws Exception {
		
		AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		
		Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://api.juno.com.br/charges"+code+"/cancelation");
		
		ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON)
				.header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
				.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
				.put(ClientResponse.class);
		
		if (clientResponse.getStatus() == 204) {
		   return "Cancelado com sucesso";	
		}
		
		return clientResponse.getEntity(String.class);
	}
	
    public String gerarBoletoApi(ObjetoPostBoletoJuno objetoPostBoletoJuno) throws Exception {
		
		Pedido pedido = pedidoRepository.findById(objetoPostBoletoJuno.getIdPedido()).get();
		
		CobrancaJunoAPI cobrancaJunoAPI = new CobrancaJunoAPI();
		
		cobrancaJunoAPI.getCharge().setPixKey(ApiTokenIntegracao.CHAVE_BOLETO_PIX);
		cobrancaJunoAPI.getCharge().setDescription(objetoPostBoletoJuno.getDescription());
		cobrancaJunoAPI.getCharge().setAmount(Float.valueOf(objetoPostBoletoJuno.getTotalAmount()));
		cobrancaJunoAPI.getCharge().setInstallments(Integer.parseInt(objetoPostBoletoJuno.getInstallments()));
		
		Calendar dataVencimento = Calendar.getInstance();
		dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
		cobrancaJunoAPI.getCharge().setDueDate(dateFormat.format(dataVencimento.getTime()));
		
		cobrancaJunoAPI.getCharge().setFine(BigDecimal.valueOf(1.00));
		cobrancaJunoAPI.getCharge().setInterest(BigDecimal.valueOf(1.00));
		cobrancaJunoAPI.getCharge().setMaxOverdueDays(10);
		cobrancaJunoAPI.getCharge().getPaymentTypes().add("BOLETO_PIX");
		
		cobrancaJunoAPI.getBilling().setName(objetoPostBoletoJuno.getPayerName());
		cobrancaJunoAPI.getBilling().setDocument(objetoPostBoletoJuno.getPayerCpfCnpj());
		cobrancaJunoAPI.getBilling().setEmail(objetoPostBoletoJuno.getEmail());
		cobrancaJunoAPI.getBilling().setPhone(objetoPostBoletoJuno.getPayerPhone());
		
		AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		
		if (accessTokenJunoAPI != null) {
			
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
			
			if (clientResponse.getStatus() == 200) { /*Retornou com sucesso*/
				
				clientResponse.close();
				objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); /*Converte relacionamento um para muitos dentro de json*/
				
				BoletoGeradoApiJuno jsonRetornoObj = objectMapper.readValue(stringRetorno,
						   new TypeReference<BoletoGeradoApiJuno>() {});
				
				int recorrencia = 1;
				
				List<BoletoJuno> boletoJunos = new ArrayList<BoletoJuno>();
				
				for (ConteudoBoletoJuno c : jsonRetornoObj.get_embedded().getCharges()) {
					
					BoletoJuno boletoJuno = new BoletoJuno();
					
					boletoJuno.setEmpresa(pedido.getEmpresaId());
					boletoJuno.setPedidoId(pedido);
					boletoJuno.setCode(c.getCode());
					boletoJuno.setLink(c.getLink());
					boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getDueDate())));
					boletoJuno.setCheckoutUrl(c.getCheckoutUrl());
					boletoJuno.setValor(new BigDecimal(c.getAmount()));
					boletoJuno.setIdChrBoleto(c.getId());
					boletoJuno.setInstallmentLink(c.getInstallmentLink());
					boletoJuno.setIdPix(c.getPix().getId());
					boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
					boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
					boletoJuno.setRecorrencia(recorrencia);
					
					boletoJunos.add(boletoJuno);
					recorrencia ++;
					
				}
				
				boletoJunoRepository.saveAllAndFlush(boletoJunos);
				
				return boletoJunos.get(0).getLink();
				
			}else {
				return stringRetorno;
			}
			
		}else {
			return "Não exite chave de acesso para a API";
		}
		
	}	
	
	public String geraChaveBoletoPix() throws Exception {
		
		AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		
		Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://api.juno.com.br/pix/keys");
		//WebResource webResource = client.resource("https://api.juno.com.br/api-integration/pix/keys");
		
		
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
				.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
				.post(ClientResponse.class, "{ \"type\": \"RANDOM_KEY\" }");
		
		         //.header("X-Idempotency-Key", "chave-boleto-pix")
		return clientResponse.getEntity(String.class);
				
	}
	

	public AccessTokenJunoAPI obterTokenApiJuno() throws Exception {
		
		AccessTokenJunoAPI accessTokenJunoAPI = accessTokenJunoService.buscaTokenAtivo();
		
		if (accessTokenJunoAPI == null || (accessTokenJunoAPI != null && accessTokenJunoAPI.expirado()) ) {
			
			String clienteID = "vi7QZerW09C8JG1o";
			String secretID = "$A_+&ksH}&+2<3VM]1MZqc,F_xif_-Dc";
			
			Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
		//Client client = new HostIgnoringCliente("https://juno.iugu.com/").hostIgnoringCliente();
			
			WebResource webResource = client.resource("https://api.juno.com.br/authorization-server/oauth/token?grant_type=client_credentials");
			//WebResource webResource = client.resource("https://juno.iugu.com/authorization-server/oauth/token?grant_type=client_credentials");			
			
			String basicChave = clienteID + ":" + secretID;
			String token_autenticacao = DatatypeConverter.printBase64Binary(basicChave.getBytes());
			
			ClientResponse clientResponse = webResource.
					accept(MediaType.APPLICATION_FORM_URLENCODED)
					.type(MediaType.APPLICATION_FORM_URLENCODED)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Authorization", "Basic " + token_autenticacao)
					.post(ClientResponse.class);
			
			if (clientResponse.getStatus() == 200) { /*Sucesso*/
				accesTokenJunoRepository.deleteAll();
				accesTokenJunoRepository.flush();
				
				AccessTokenJunoAPI accessTokenJunoAPI2 = clientResponse.getEntity(AccessTokenJunoAPI.class);
				accessTokenJunoAPI2.setToken_acesso(token_autenticacao);
				
				accessTokenJunoAPI2 = accesTokenJunoRepository.saveAndFlush(accessTokenJunoAPI2);
				
				return accessTokenJunoAPI2;
			}else {
				return null;
			}
			
			
		}else {
			return accessTokenJunoAPI;
		}
	}

	/*
	 * {"id":"wbh_AE815607C1F5A94934934A2EA3CA0180","url":"https://lojavirtual-env.eba-emeh4pnx.sa-east-1.elasticbeanstalk.com/lojavirtual/requisicaojunoboleto/notificacaoapiv2","secret":"23b85f4998289533ed3ee310ae9d5bd3f803fadac7fb1ecff0296fbf1bb060f8","status":"ACTIVE","eventTypes":[{"id":"evt_DC2E7E8848B08C62","name":"DOCUMENT_STATUS_CHANGED","label":"O status de um documento foi alterado","status":"ENABLED"}],"_links":{"self":{"href":"https://api.juno.com.br/api-integration/notifications/webhooks/wbh_AE815607C1F5A94934934A2EA3CA0180"}}}
	 * {"id":"wbh_AE815607C1F5A94934934A2EA3CA0180","url":"https://lojavirtualmentoria-env.eba-bijtuvkg.sa-east-1.elasticbeanstalk.com/loja_virtual_mentoria/requisicaojunoboleto/notificacaoapiv2","secret":"23b85f4998289533ed3ee310ae9d5bd3f803fadac7fb1ecff0296fbf1bb060f8","status":"ACTIVE","eventTypes":[{"id":"evt_DC2E7E8848B08C62","name":"DOCUMENT_STATUS_CHANGED","label":"O status de um documento foi alterado","status":"ENABLED"}],"_links":{"self":{"href":"https://api.juno.com.br/api-integration/notifications/webhooks/wbh_AE815607C1F5A94934934A2EA3CA0180"}}}
	 * 
	 * */	
	public String criarWebHook(CriarWebHook criarWebHook) throws Exception {
		
	    AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		
		Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://api.juno.com.br/notifications/webhooks");
		
		String json = new ObjectMapper().writeValueAsString(criarWebHook);
		
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
				.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
				.post(ClientResponse.class, json);
		
		 String resposta = clientResponse.getEntity(String.class);
	
		 clientResponse.close();
		
		return resposta;
		
	}	
	
	public String listaWebHook() throws Exception {

	    AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		
		Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://api.juno.com.br/notifications/webhooks");

		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
				.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
				.get(ClientResponse.class);
		
		 String resposta = clientResponse.getEntity(String.class);
		
		 return resposta;
		 
	}
	
	public void deleteWebHook(String idWebHook) throws Exception {

	    AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		
		Client client = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://api.juno.com.br/notifications/webhooks/" + idWebHook);

		webResource
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
				.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
				.delete();		 
	}
	
}
