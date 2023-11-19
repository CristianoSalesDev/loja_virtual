package com.br.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.br.lojavirtual.controller.PessoaController;
import com.br.lojavirtual.enums.TipoEndereco;
import com.br.lojavirtual.model.Endereco;
import com.br.lojavirtual.model.PessoaFisica;
import com.br.lojavirtual.model.PessoaJuridica;
import com.br.lojavirtual.model.dto.ObjetoPostBoletoJuno;
import com.br.lojavirtual.repository.PessoaRepository;
import com.br.lojavirtual.service.ServiceJunoBoleto;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ServiceJunoBoleto serviceJunoBoleto;
	
	@Test
	public void testeToken() throws Exception {
		serviceJunoBoleto.obterTokenApiJuno();
	}
	
	@Test
	public void testeToken2() throws Exception {
	  String valor = serviceJunoBoleto.geraChaveBoletoPix();
	  System.out.println(valor);
	}	
	
	@Test
	public void testeToken3() throws Exception {
      ObjetoPostBoletoJuno objetoPostCarneJuno = new ObjetoPostBoletoJuno();
 	 objetoPostCarneJuno.setDescription("Teste de geração de boleto e pix");
 	 objetoPostCarneJuno.setEmail("cristianoaragaosales@gmail.com");
 	 objetoPostCarneJuno.setIdPedido(21L);
 	 objetoPostCarneJuno.setInstallments("6");
 	 objetoPostCarneJuno.setPayerCpfCnpj("05916564937");
 	 objetoPostCarneJuno.setPayerName("Alex fernando");
 	 objetoPostCarneJuno.setPayerPhone("45999795800");
 	 objetoPostCarneJuno.setReference("Venda de venda de loja virtual cod: 21");
 	 objetoPostCarneJuno.setTotalAmount("50.00");      
	  String valor = serviceJunoBoleto.gerarBoletoApi(objetoPostCarneJuno);
	  System.out.println(valor);
	}	
	
	@Test
	public void testCadPessoaJuridica() throws ExceptionLojaVirtual {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();		
		pessoaJuridica.setCnpj("32.093.900/0001-17");
		pessoaJuridica.setNome("Cristiano Aragão");
		pessoaJuridica.setEmail("abc12.app@gmail.com");
		pessoaJuridica.setTelefone("45999795800");
		pessoaJuridica.setInscricaoEstadual("807801286");
		pessoaJuridica.setInscricaoMunicipal("55554565656565");
		pessoaJuridica.setNomeFantasia("54556565665");
		pessoaJuridica.setRazaoSocial("4656656566");
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Jd Dias");
		endereco1.setCep("556556565");
		endereco1.setComplemento("Casa cinza");
		endereco1.setEmpresaId(pessoaJuridica);
		endereco1.setNumero("389");
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setLogradouro("Av. são joao sexto");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("PR");
		endereco1.setCidade("Curitiba");
		
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Jd Maracana");
		endereco2.setCep("7878778");
		endereco2.setComplemento("Andar 4");
		endereco2.setEmpresaId(pessoaJuridica);
		endereco2.setNumero("555");
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setLogradouro("Av. maringá");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("PR");
		endereco2.setCidade("Curitiba");
		
		pessoaJuridica.getEnderecos().add(endereco2);
		pessoaJuridica.getEnderecos().add(endereco1);

		pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0 );
		
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());
		
	}	
	
	@Test
	public void testCadPessoaFisica() throws ExceptionLojaVirtual {
		
		PessoaJuridica pessoaJuridica = pessoaRepository.existeCnpjCadastrado("1690937368885");
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCpf("474.416.130-86");
		pessoaFisica.setNome("Aragão");
		pessoaFisica.setEmail("portifolio7.app@gmail.com");
		pessoaFisica.setTelefone("85989009924");
		pessoaFisica.setEmpresaId(pessoaJuridica);
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Jd Dias");
		endereco1.setCep("556556565");
		endereco1.setComplemento("Casa cinza");
		endereco1.setNumero("389");
		endereco1.setPessoa(pessoaFisica);
		endereco1.setLogradouro("Av. são joao sexto");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("PR");
		endereco1.setCidade("Curitiba");
		endereco1.setEmpresaId(pessoaJuridica);
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Jd Maracana");
		endereco2.setCep("7878778");
		endereco2.setComplemento("Andar 4");
		endereco2.setNumero("555");
		endereco2.setPessoa(pessoaFisica);
		endereco2.setLogradouro("Av. maringá");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("PR");
		endereco2.setCidade("Curitiba");
		endereco2.setEmpresaId(pessoaJuridica);
		
		pessoaFisica.getEnderecos().add(endereco2);
		pessoaFisica.getEnderecos().add(endereco1);

		pessoaFisica = pessoaController.salvarPf(pessoaFisica).getBody();
		
		assertEquals(true, pessoaFisica.getId() > 0 );
		
		for (Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaFisica.getEnderecos().size());
		
	}
	
}
