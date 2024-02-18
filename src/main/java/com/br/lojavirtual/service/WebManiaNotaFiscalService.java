package com.br.lojavirtual.service;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.br.lojavirtual.model.dto.WebManiaNotaFicalEletronica;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class WebManiaNotaFiscalService implements Serializable {

	private static final long serialVersionUID = 1L;

	public String emitirNotaFiscal(WebManiaNotaFicalEletronica webManiaNotaFicalEletronica) throws Exception {
		
		Client client = new HostIgnoringCliente("https://webmaniabr.com/api/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/emissao/");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(webManiaNotaFicalEletronica);
		
		
		ClientResponse clientResponse =  webResource
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("X-Consumer-Key", "N6scITMtiEunElnEUAVMQpRAhJrHgU0e")
				.header("X-Consumer-Secret", "6fBypsFYfQCTt6LBAkRjuGm6nHimRWoNCOiQ9aqLkcNEtHLR")
				.header("X-Access-Token", "3554-LLWm2CoGsTzxXW0QiLPmG2ha2f4lSO6hA3USo2IprD6J9Wwy")
				.header("X-Access-Token-Secret", "6RGm39tdeCYEgIvOKLuSdrGbiuK769Lx0Ngy3iueCVXCDP0E")
				.post(ClientResponse.class, json);
		
		String stringRetorno = clientResponse.getEntity(String.class);
		
		return stringRetorno;
		
	}
	
}
