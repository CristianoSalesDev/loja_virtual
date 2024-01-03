package com.br.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.br.lojavirtual.model.dto.CriarWebHook;
import com.br.lojavirtual.service.ServiceJunoBoleto;

import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TesteJunoBoleto extends TestCase {

	@Autowired
	private ServiceJunoBoleto serviceJunoBoleto;
	
	@Test
	public void testeCriarWebHook() throws Exception {
		
		CriarWebHook criarWebHook = new CriarWebHook();
		criarWebHook.setUrl("https://lojavirtual-env.eba-emeh4pnx.sa-east-1.elasticbeanstalk.com/lojavirtual/requisicaojunoboleto/notificacaoapiv2");
		criarWebHook.getEventTypes().add("PAYMENT_NOTIFICATION");
		criarWebHook.getEventTypes().add("BILL_PAYMENT_STATUS_CHANGED");
		
		String retorno = serviceJunoBoleto.criarWebHook(criarWebHook);
		
		System.out.println(retorno);
	}	
}
