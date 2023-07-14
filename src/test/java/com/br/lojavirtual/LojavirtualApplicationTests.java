package com.br.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.br.lojavirtual.controller.AcessoController;
import com.br.lojavirtual.model.Acesso;

@SpringBootTest(classes = LojavirtualApplication.class)
public class LojavirtualApplicationTests {
	
	@Autowired
	private AcessoController acessoController;
	
	@Test
	public void testCadastraAcesso() {
		
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		acessoController.salvarAcesso(acesso);
	}

}
