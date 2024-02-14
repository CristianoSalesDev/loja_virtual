package com.br.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.br.lojavirtual.model.dto.CriarWebHook;
import com.br.lojavirtual.model.dto.ObjetoPostCarneJuno;
import com.br.lojavirtual.service.ServiceJunoBoleto;

import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TesteJunoBoleto extends TestCase {

	@Autowired
	private ServiceJunoBoleto serviceJunoBoleto;
	
	@Test
	public void testgerarBoletoApiAsaas() throws Exception {
		
		ObjetoPostCarneJuno dados = new ObjetoPostCarneJuno();
		dados.setEmail("cristianoaragaosales@gmail.com");
		dados.setPayerName("Cristiano de Aragão Sales");
		dados.setPayerCpfCnpj("54636299353");
		dados.setPayerPhone("85999815724");
		dados.setIdVenda(20L);
		
		String retorno = serviceJunoBoleto.gerarBoletoApiAsaas(dados);
		
		System.out.println(retorno);
	}
	
	@Test
	public void testbuscaClientePessoaApiAsaas()  throws Exception{
		
		ObjetoPostCarneJuno dados = new ObjetoPostCarneJuno();
		dados.setEmail("cristianoaragaosales@gmail.com");
		dados.setPayerName("Cristiano de Aragão Sales");
		dados.setPayerCpfCnpj("54636299353");
		dados.setPayerPhone("85999815724");
		
//		serviceJunoBoleto.buscaClientePessoaApiAsaas(dados);
		String  customer_id =serviceJunoBoleto.buscaClientePessoaApiAsaas(dados);
		
		assertEquals("cus_000078450786", customer_id);
	}
	
	@Test
	public void testcriarChavePixAsaas() throws Exception {
		
		String chaveAPi = serviceJunoBoleto.criarChavePixAsaas();
		
		System.out.println("Chave Asass API" + chaveAPi);
	}	
	
	@Test
	public void listaWebHook() throws Exception {
		
		String retorno = serviceJunoBoleto.listaWebHook();
		
		System.out.print(retorno);
	}
	
	@Test
	public void deleteWebHook() throws Exception {
		
		serviceJunoBoleto.deleteWebHook("wbh_E71095B5BF65E8D2DB018EE8A89BACB8");
		
	}
	
	
	@Test
	public void testeCriarWebHook() throws Exception {
		
		CriarWebHook criarWebHook = new CriarWebHook();
		criarWebHook.setUrl("https://api.comprefacilnahora.com.br/lojavirtual/requisicaojunoboleto/notificacaoapiv2");
		criarWebHook.getEventTypes().add("PAYMENT_NOTIFICATION");
		criarWebHook.getEventTypes().add("BILL_PAYMENT_STATUS_CHANGED");
		
		String retorno = serviceJunoBoleto.criarWebHook(criarWebHook);
		
		System.out.println(retorno);
	}	
}
