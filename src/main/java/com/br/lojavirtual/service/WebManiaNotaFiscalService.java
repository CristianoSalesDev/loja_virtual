package com.br.lojavirtual.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.lojavirtual.model.NFe;
import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.model.dto.ObjetoDevolucaoNotaFiscalWebMania;
import com.br.lojavirtual.model.dto.ObjetoEmissaoNotaFiscalWebMania;
import com.br.lojavirtual.model.dto.ObjetoEstornoNotaFiscalWebMania;
import com.br.lojavirtual.model.dto.WebManiaNotaFicalEletronica;
import com.br.lojavirtual.repository.NfeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class WebManiaNotaFiscalService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private NfeRepository nfeRepository; 	

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
	
	public String cancelarNotaFiscal(String uuid, String motivo) throws Exception {
		
		Client client = new HostIgnoringCliente("https://webmaniabr.com/api/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/cancelar/");
		
		String json = "{\"uuid\":\""+uuid+"\",\"motivo\":\""+motivo+"\"}";
		
		
		ClientResponse clientResponse = webResource
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("X-Consumer-Key", "N6scITMtiEunElnEUAVMQpRAhJrHgU0e")
				.header("X-Consumer-Secret", "6fBypsFYfQCTt6LBAkRjuGm6nHimRWoNCOiQ9aqLkcNEtHLR")
				.header("X-Access-Token", "3554-LLWm2CoGsTzxXW0QiLPmG2ha2f4lSO6hA3USo2IprD6J9Wwy")
				.header("X-Access-Token-Secret", "6RGm39tdeCYEgIvOKLuSdrGbiuK769Lx0Ngy3iueCVXCDP0E")
				.put(ClientResponse.class, json);
		
		String stringRetorno = clientResponse.getEntity(String.class);
		
		return stringRetorno;
		
	}
	
	public String consultarNotaFiscal(String uuid) throws Exception {
		
		Client client = new HostIgnoringCliente("https://webmaniabr.com/api/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/consulta/");
		
		
		
		ClientResponse clientResponse = webResource.queryParam("uuid", uuid)
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("X-Consumer-Key", "N6scITMtiEunElnEUAVMQpRAhJrHgU0e")
				.header("X-Consumer-Secret", "6fBypsFYfQCTt6LBAkRjuGm6nHimRWoNCOiQ9aqLkcNEtHLR")
				.header("X-Access-Token", "3554-LLWm2CoGsTzxXW0QiLPmG2ha2f4lSO6hA3USo2IprD6J9Wwy")
				.header("X-Access-Token-Secret", "6RGm39tdeCYEgIvOKLuSdrGbiuK769Lx0Ngy3iueCVXCDP0E")
				.get(ClientResponse.class);
		
		String stringRetorno = clientResponse.getEntity(String.class);
		
		return stringRetorno;
		
	}
	
	public String estornoNotaFiscal(ObjetoEstornoNotaFiscalWebMania estorno) throws Exception {
		
		Client client = new HostIgnoringCliente("https://webmaniabr.com/api/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/devolucao/");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(estorno);
		
		ClientResponse clientResponse = webResource
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
	
	public String devolucaoNotaFiscal(ObjetoDevolucaoNotaFiscalWebMania devolucao) throws Exception {
		
		Client client = new HostIgnoringCliente("https://webmaniabr.com/api/").hostIgnoringCliente();
		WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/devolucao/");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(devolucao);
		
		ClientResponse clientResponse = webResource
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
	
	public NFe gravaNotaParaVenda(ObjetoEmissaoNotaFiscalWebMania emissaoNotaFiscalWebMania, Pedido pedido) {
		
		NFe notaFiscalVendaBusca = nfeRepository.consultarNotaFiscalPorVendaUnica(pedido.getId());
		
		NFe notaFiscalVenda = new NFe();
		
		if (notaFiscalVendaBusca != null && notaFiscalVendaBusca.getId() > 0) {
			notaFiscalVenda.setId(notaFiscalVendaBusca.getId());
		}
		
		notaFiscalVenda.setEmpresaId(pedido.getEmpresaId());
		notaFiscalVenda.setNumero(emissaoNotaFiscalWebMania.getUuid());
		notaFiscalVenda.setPdf(emissaoNotaFiscalWebMania.getDanfe());
		notaFiscalVenda.setSerie(emissaoNotaFiscalWebMania.getSerie());
		notaFiscalVenda.setTipo(emissaoNotaFiscalWebMania.getModelo());
		notaFiscalVenda.setPedidoId(pedido);
		notaFiscalVenda.setXml(emissaoNotaFiscalWebMania.getXml());
		notaFiscalVenda.setChave(emissaoNotaFiscalWebMania.getChave());
		
		return nfeRepository.saveAndFlush(notaFiscalVenda);
		
	}
	
	
}
