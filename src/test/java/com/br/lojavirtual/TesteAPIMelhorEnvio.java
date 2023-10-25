package com.br.lojavirtual;

import com.br.lojavirtual.enums.ApiTokenIntegracao;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TesteAPIMelhorEnvio {

	public static void main(String[] args) throws Exception {
		
	/*
		OkHttpClient client = new OkHttpClient().newBuilder() .build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{ \"from\": { \"postal_code\": \"96020360\" }, \"to\": { \"postal_code\": \"01018020\" }, \"products\": [ { \"id\": \"x\", \"width\": 11, \"height\": 17, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 10.1, \"quantity\": 1 }, { \"id\": \"y\", \"width\": 16, \"height\": 25, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 55.05, \"quantity\": 2 }, { \"id\": \"z\", \"width\": 22, \"height\": 30, \"length\": 11, \"weight\": 1, \"insurance_value\": 30, \"quantity\": 1 } ] }");
			Request request = new Request.Builder()
			  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX +"api/v2/me/shipment/calculate")
			  .method("POST", body)
			  .addHeader("Accept", "application/json")
			  .addHeader("Content-Type", "application/json")
			  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
			  .addHeader("User-Agent", "cristianoaragaosales@gmail.com")
			  .build();
			
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string()); 
			
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
			
			System.out.println(empresaTransporteDTOs);		
	*/
		
		
		/* Insere as etiquetas do frete 
		
		OkHttpClient client = new OkHttpClient().newBuilder() .build();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"service\":0,\"agency\":49,\"from\":{\"name\":\"string\",\"phone\":\"string\",\"email\":\"string\",\"document\":\"string\",\"company_document\":\"string\",\"state_register\":\"string\",\"address\":\"string\",\"complement\":\"string\",\"number\":\"string\",\"district\":\"string\",\"city\":\"string\",\"country_id\":\"string\",\"postal_code\":\"string\",\"state_abbr\":\"string\",\"note\":\"string\"},\"to\":{\"name\":\"string\",\"phone\":\"string\",\"email\":\"string\",\"document\":\"string\",\"company_document\":\"string\",\"state_register\":\"string\",\"address\":\"string\",\"complement\":\"string\",\"number\":\"string\",\"district\":\"string\",\"city\":\"string\",\"country_id\":\"string\",\"postal_code\":\"string\",\"state_abbr\":\"string\",\"note\":\"string\"},\"products\":[{\"name\":\"string\",\"quantity\":\"string\",\"unitary_value\":\"string\"}],\"volumes\":[{\"height\":0,\"width\":0,\"length\":0,\"weight\":0}],\"options\":{\"insurance_value\":0,\"receipt\":true,\"own_hand\":true,\"reverse\":true,\"non_commercial\":true,\"invoice\":{\"key\":\"string\"},\"plataform\":\"string\",\"tags\":[{\"tag\":\"string\",\"Url\":\"string\"}]}}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/cart")
		  //.post(body)
		  .method("POST", body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "cristianoaragaosales@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());
		
		*/
		
		/*
		   OkHttpClient client = new OkHttpClient().newBuilder() .build();
	
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"options\":{\"receipt\":true,\"own_hand\":true,\"reverse\":true,\"non_commercial\":true}}");
			Request request = new Request.Builder()
			  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/cart")
			  .method("POST", body)
			  .addHeader("Accept", "application/json")
			  .addHeader("Content-Type", "application/json")
			  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
			  .addHeader("User-Agent", "cristianoaragaosales@gmail.com")
			  .build();
			
			Response response = client.newCall(request).execute();
			
		    System.out.println(response.body().string());
        */
		
		/* Faz a compra do frete para as etiquetas */
		
		 /*
			OkHttpClient client = new OkHttpClient().newBuilder() .build();
				
			  MediaType mediaType = MediaType.parse("application/json");
			  RequestBody body = RequestBody.create(mediaType, "{ \"from\": { \"postal_code\": \"96020360\" }, \"to\": { \"postal_code\": \"01018020\" }, \"products\": [ { \"id\": \"x\", \"width\": 11, \"height\": 17, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 10.1, \"quantity\": 1 }, { \"id\": \"y\", \"width\": 16, \"height\": 25, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 55.05, \"quantity\": 2 }, { \"id\": \"z\", \"width\": 22, \"height\": 30, \"length\": 11, \"weight\": 1, \"insurance_value\": 30, \"quantity\": 1 } ] }");			
			  Request request = new Request.Builder()
			  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/shipment/checkout")
			  .method("POST", body)
			  //.post(null)
			  .addHeader("Accept", "application/json")
			  .addHeader("Content-Type", "application/json")
			  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
			  .addHeader("User-Agent", "cristianoaragaosales@gmail.com")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			System.out.println(response.body().string());
	    */
		
	    /* Gera as etiquetas e imprime 	*/ 
		
		OkHttpClient client = new OkHttpClient().newBuilder() .build();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"mode\":\"\",\"orders\":[\"{{order_id}}\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/shipment/print")
		  //.method("POST", body)
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "cristianoaragaosales@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());
	
		
		/*
			OkHttpClient client = new OkHttpClient().newBuilder() .build();
	
			Request request = new Request.Builder()
			  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/shipment/generate")
			  .method("POST", body)
			  .addHeader("Accept", "application/json")
			  .addHeader("Content-Type", "application/json")
			  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
			  .addHeader("User-Agent", "cristianoaragaosales@gmail.com")
			  .build();
	
			Response response = client.newCall(request).execute();
	   */			
}
	
}
