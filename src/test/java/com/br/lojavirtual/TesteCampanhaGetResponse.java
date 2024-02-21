package com.br.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.br.lojavirtual.model.dto.CampanhaGetResponse;
import com.br.lojavirtual.model.dto.LeadCampanhaGetResponse;
import com.br.lojavirtual.model.dto.LeadCampanhaGetResponseCadastro;
import com.br.lojavirtual.model.dto.ObjetoFromFieldIdGetResponse;
import com.br.lojavirtual.service.ServiceGetResponseEmailMarketing;

import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = LojavirtualApplication.class)
public class TesteCampanhaGetResponse extends TestCase {

		@Autowired
		private ServiceGetResponseEmailMarketing serviceGetResponseEmailMarketing; 
		
		@Test
		public void testCarregaCampanhaGetResponse() throws Exception {
			
		   List<CampanhaGetResponse> list = serviceGetResponseEmailMarketing.carregaListaCampanhaGetResponse();
			
			for (CampanhaGetResponse campanhaGetResponse : list) {
				System.out.println(campanhaGetResponse);
				System.out.println("---------------------------");
			}  	 		
	}
	
	@Test
	public void testCriaLead() throws Exception {
		
		LeadCampanhaGetResponse lead = new LeadCampanhaGetResponse();
		
		lead.setName("Cristiano Aragão teste api");
		lead.setEmail("qbronzespa@gmail.com");
		
		LeadCampanhaGetResponseCadastro campanha = new LeadCampanhaGetResponseCadastro();
		campanha.setCampaignId("jzyv8");
		lead.setCampaign(campanha);		
		
		String retorno = serviceGetResponseEmailMarketing.criaLeadApiGetResponse(lead);
		
		System.out.println(retorno);
		
	}
	
	@Test
	public void testEnviaEmailporAPI() throws Exception {
		
		String retorno = serviceGetResponseEmailMarketing.
				enviaEmailApiGetResponse("jzyv8",  "Teste de e-mail", "<html><body>text do email</body></html>");
		
		System.out.println(retorno);				
	}
	
	@Test
	public void testBuscaFromFielId() throws Exception {
		
		List<ObjetoFromFieldIdGetResponse> fieldIdGetResponses = serviceGetResponseEmailMarketing.listaFromFieldId();
		
		for (ObjetoFromFieldIdGetResponse objetoFromFieldIdGetResponse : fieldIdGetResponses) {
			System.out.println(objetoFromFieldIdGetResponse);
		}
	}
	
}
