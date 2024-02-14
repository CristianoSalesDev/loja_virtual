package com.br.lojavirtual.model.dto;

/**
 * 
 * Armazena URL da API de Key da chave e tipos de pagamento
 * @author Cristiano Aragão
 *
 */

public class AsaasApiPagamentoStatus {
	
	public static String BOLETO = "BOLETO";
	public static String CREDIT_CARD = "CREDIT_CARD";
	public static String PIX = "PIX";
	public static String BOLETO_PIX = "UNDEFINED"; /*cobrança que pode ser paga por pix, boleto e cartão*/
	
	public static String URL_API_ASAAS = "https://www.asaas.com/api/v3/";
	
	public static String API_KEY = "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAzOTQ2Njk6OiRhYWNoXzY4NzQyM2RkLTM4NzYtNDJmMS1iNDEwLTZiZDI1MWQ1NDQ5OQ==";
	
   /* chave API_KEY inválida */	
// public static String API_KEY = "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAzMDkyNzA6OiRhYWNoX2RhODE2ODY5LTU0NGEtNDgwYS05MTczLWEzYzliOTkzMTk3Zg==";	
	
}
