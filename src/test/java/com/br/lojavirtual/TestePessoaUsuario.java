package com.br.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.br.lojavirtual.controller.PessoaController;
import com.br.lojavirtual.model.PessoaJuridica;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testCadPessoaFisica() throws ExceptionLojaVirtual {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Cristiano Aragão");
		pessoaJuridica.setEmail("cristianoaragaosales@gmail.com");
		pessoaJuridica.setTelefone("45999795800");
		pessoaJuridica.setInscricaoEstadual("65556565656665");
		pessoaJuridica.setInscricaoMunicipal("55554565656565");
		pessoaJuridica.setNomeFantasia("54556565665");
		pessoaJuridica.setRazaoSocial("4656656566");
		
//		pessoaRepository.save(pessoaJuridica);
		
		pessoaController.salvarPj(pessoaJuridica);
		
		/*
		PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setCpf("0597975788");
		pessoaFisica.setNome("Alex fernando");
		pessoaFisica.setEmail("alex.fernando.egidio@gmail.com");
		pessoaFisica.setTelefone("45999795800");
		pessoaFisica.setEmpresa(pessoaFisica);*/
		
	}
	
}
