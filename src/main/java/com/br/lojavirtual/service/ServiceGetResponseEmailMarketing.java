package com.br.lojavirtual.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.br.lojavirtual.enums.ApiTokenIntegracao;
import com.br.lojavirtual.model.dto.CampanhaGetResponse;
import com.br.lojavirtual.model.dto.LeadCampanhaGetResponse;
import com.br.lojavirtual.model.dto.NewsLetterGetResponse;
import com.br.lojavirtual.model.dto.ObjetoFromFieldIdGetResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class ServiceGetResponseEmailMarketing implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<CampanhaGetResponse> carregaListaCampanhaGetResponse() throws Exception {
		
		Client client = new HostIgnoringCliente(ApiTokenIntegracao.URL_END_POINT_GET_RESPONSE).hostIgnoringCliente();
			
			String json = client.resource(ApiTokenIntegracao.URL_END_POINT_GET_RESPONSE + "campaigns")
					.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE)
					.get(String.class);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			
			List<CampanhaGetResponse> list = objectMapper.readValue(json, 
					new TypeReference<List<CampanhaGetResponse>>() {});
			
			return list;	
    }
	
	public String criaLeadApiGetResponse(LeadCampanhaGetResponse leadCampanhaGetResponse) throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(leadCampanhaGetResponse);
		
		Client client = new HostIgnoringCliente(ApiTokenIntegracao.URL_END_POINT_GET_RESPONSE)
				                .hostIgnoringCliente();
		
		WebResource webResource = client.resource(ApiTokenIntegracao.URL_END_POINT_GET_RESPONSE + "contacts");
		
		ClientResponse clientResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE)
				.post(ClientResponse.class, json);
		
		String retorno = clientResponse.getEntity(String.class);
		
		if (clientResponse.getStatus() == 202) {
			retorno = "Cadastrado com sucesso";
		}
		
		clientResponse.close();
		return retorno;
	}
	
	public String enviaEmailApiGetResponse(String idCampanha, String nomeEmail, String msg) throws Exception {
		
		NewsLetterGetResponse newsLetterGetResponse = new NewsLetterGetResponse();
		newsLetterGetResponse.getSendSettings().getSelectedCampaigns().add(idCampanha);/* jzyv8 - Campanha e lista de e-mail para qual será enviado o e-mail*/
		newsLetterGetResponse.setSubject(nomeEmail);
		newsLetterGetResponse.setName(newsLetterGetResponse.getSubject());
		newsLetterGetResponse.getReplyTo().setFromFieldId("o06MU");/*ID email para resposta*/
		newsLetterGetResponse.getFromField().setFromFieldId("o06MU");/*ID do e-mail do remetente*/
		newsLetterGetResponse.getCampaign().setCampaignId("jzyv8");/*Campanha de origem, campanha pai*/
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate hoje = LocalDate.now();
		LocalDate amanha = hoje.plusDays(1);
		String dataEnvio = amanha.format(dateTimeFormatter);
		
		newsLetterGetResponse.setSendOn(dataEnvio + "T15:20:52-03:00");
		
		newsLetterGetResponse.getContent().setHtml(msg);
		
		String json = new ObjectMapper().writeValueAsString(newsLetterGetResponse);
		
		
		Client client = new HostIgnoringCliente(ApiTokenIntegracao.URL_END_POINT_GET_RESPONSE).hostIgnoringCliente();
		
		WebResource webResource = client.resource(ApiTokenIntegracao.URL_END_POINT_GET_RESPONSE + "newsletters");
		
		ClientResponse clientResponse = webResource
		.accept(MediaType.APPLICATION_JSON)
		.type(MediaType.APPLICATION_JSON)
		.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE)
		.post(ClientResponse.class, json);
		
		String retorno = clientResponse.getEntity(String.class);
		
		if (clientResponse.getStatus() == 201) {
			retorno = "Enviado com sucesso";
		}
		
		clientResponse.close();
		
		return retorno;
	}
	
public List<ObjetoFromFieldIdGetResponse> listaFromFieldId() throws Exception{
		
		Client client = new HostIgnoringCliente(ApiTokenIntegracao.URL_END_POINT_GET_RESPONSE).hostIgnoringCliente();
		
		WebResource webResource = client.resource(ApiTokenIntegracao.URL_END_POINT_GET_RESPONSE + "from-fields");
		
		String clientResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE)
				.get(String.class);
		
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		
		List<ObjetoFromFieldIdGetResponse> list = objectMapper.readValue(clientResponse, 
				new TypeReference<List<ObjetoFromFieldIdGetResponse>>() {});
		
		return list;
		
	}
	
}
