package com.br.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.br.lojavirtual.model.dto.WebManiaClienteNF;
import com.br.lojavirtual.model.dto.WebManiaNotaFicalEletronica;
import com.br.lojavirtual.model.dto.WebManiaPedidoNF;
import com.br.lojavirtual.model.dto.WebManiaProdutoNF;
import com.br.lojavirtual.service.WebManiaNotaFiscalService;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TesteNotaFiscal extends TestCase {
	
	@Autowired
	private WebManiaNotaFiscalService webManiaNotaFiscalService;
	
	@Test
	public void testeEmissaoNota() throws Exception {
	
		emiteNotaFiscal();
	}	

	private String emiteNotaFiscal() throws Exception {
		WebManiaNotaFicalEletronica webManiaNotaFicalEletronica = new WebManiaNotaFicalEletronica();
		
		/*Dados da nota*/
		webManiaNotaFicalEletronica.setID("1");
		webManiaNotaFicalEletronica.setUrl_notificacao("");/*WebHook*/
		webManiaNotaFicalEletronica.setOperacao(1); /*Saída*/
		webManiaNotaFicalEletronica.setNatureza_operacao("Venda de celular Iphone 13");
		webManiaNotaFicalEletronica.setModelo("1"); /* NF-e*/
		webManiaNotaFicalEletronica.setFinalidade(1); /* NF-e normal*/
		webManiaNotaFicalEletronica.setAmbiente(2); /*Homologação*/
		
		/*Dados do cliente que está comprando*/
		WebManiaClienteNF cliente = new WebManiaClienteNF();
		cliente.setBairro("JD Dias 1");
		cliente.setCep("87025758");
		cliente.setCidade("Maringá");
		cliente.setComplemento("NA");
		cliente.setCpf("05916564937");
		cliente.setEmail("alex.fernando.egidio@gmail.com");
		cliente.setEndereco("Pioneiro antonio de ganello");
		cliente.setNumero("356");
		cliente.setTelefone("45999795800");
		cliente.setUf("PR");
		cliente.setNome_completo("Alex Fernando Egidio");
		
		webManiaNotaFicalEletronica.setCliente(cliente);
		
		/*Dados do Produto*/
		WebManiaProdutoNF produto = new WebManiaProdutoNF();
		
		produto.setNome("Iphone 13");
		produto.setCodigo("1111");
		produto.setNcm("6109.10.00");
		produto.setCest("28.038.00");
		produto.setQuantidade(1);
		produto.setUnidade("UN");
		produto.setPeso("0.800");
		produto.setOrigem(0);
		produto.setSubtotal("1500");
		produto.setTotal("1500");
		produto.setClasse_imposto("REF57569972");
		
        WebManiaProdutoNF produto2 = new WebManiaProdutoNF();
		
		produto2.setNome("Sansung galaxy 13");
		produto2.setCodigo("11112");
		produto2.setNcm("6109.10.00");
		produto2.setCest("28.038.00");
		produto2.setQuantidade(1);
		produto2.setUnidade("UN");
		produto2.setPeso("0.800");
		produto2.setOrigem(0);
		produto2.setSubtotal("1500");
		produto2.setTotal("1500");
		produto2.setClasse_imposto("REF57569972");
		
		webManiaNotaFicalEletronica.getProdutos().add(produto);
		webManiaNotaFicalEletronica.getProdutos().add(produto2);
		
		WebManiaPedidoNF pedidoNF = new WebManiaPedidoNF();
		
		pedidoNF.setPagamento(0); /* á vista*/
		pedidoNF.setPresenca(2); /* pela internet*/
		pedidoNF.setModalidade_frete(0);
		pedidoNF.setFrete("60");
		pedidoNF.setDesconto("120");
		pedidoNF.setTotal("2940");
		
		webManiaNotaFicalEletronica.setPedido(pedidoNF);
		
		String retorno = webManiaNotaFiscalService.emitirNotaFiscal(webManiaNotaFicalEletronica);
		
		System.out.println("-------->> Retorno Emissão nota fiscal: " + retorno);
		
		return retorno;
	}	

}
