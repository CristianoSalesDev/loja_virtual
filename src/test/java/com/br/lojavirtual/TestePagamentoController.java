package com.br.lojavirtual;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.br.lojavirtual.controller.PagamentoController;
import com.br.lojavirtual.controller.RecebePagamentoWebHookApiJuno;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TestePagamentoController extends TestCase {

	@Autowired
	private PagamentoController pagamentoController;
	
	@Autowired
	private RecebePagamentoWebHookApiJuno recebePagamentoWebHookApiJuno;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Test
	public void testRecebeNotificacaoPagamentoApiAsaas() throws Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(wac);
		MockMvc mockMvc = builder.build();
		
		String json = new String(Files.readAllBytes(Paths.get("C:\\Users\\Administrador\\Documents\\workspace-spring-tool-suite-4-4.17.2.RELEASE\\lojavirtual\\src\\test\\java\\com\\br\\lojavirtual\\jsonwebhookasaas.txt")));
		
		ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.post("/requisicaojunoboleto/notificacaoapiasaas")
				.content(json)
				.accept("application/json;charset=UTF-8")
				.contentType("application/json;charset=UTF-8"));
		
	 System.out.println(retornoApi.andReturn().getRequest().getContentAsString());		
		
	}	
	
	@Test
	public void testfinalizarCompraCartaoAsaas() throws Exception {
		pagamentoController.finalizarCompraCartaoAsaas("5502095358379859", "Cristiano A Sales",
													  "423", "02",
													  "2031", 20L, "54636299353",
													  2, "60766245", "Rua Sargento Neri",
													  "265", "CE", "Fortaleza");
	}
}
