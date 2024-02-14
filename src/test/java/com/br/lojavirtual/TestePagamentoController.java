package com.br.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.br.lojavirtual.controller.PagamentoController;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TestePagamentoController extends TestCase {

	@Autowired
	private PagamentoController pagamentoController;
	
	@Test
	public void testfinalizarCompraCartaoAsaas() throws Exception {
		pagamentoController.finalizarCompraCartaoAsaas("5502095358379859", "Cristiano A Sales",
													  "423", "02",
													  "2031", 20L, "54636299353",
													  2, "60766245", "Rua Sargento Neri",
													  "265", "CE", "Fortaleza");
	}
}
