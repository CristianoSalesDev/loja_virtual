package com.br.lojavirtual.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.lojavirtual.ExceptionLojaVirtual;
import com.br.lojavirtual.model.Produto;
import com.br.lojavirtual.repository.ProdutoRepository;
import com.br.lojavirtual.service.ServiceSendEmail;

@Controller
@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail; 
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/salvarProduto") /*Mapeando a url para receber JSON*/
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaVirtual, MessagingException, IOException { /*Recebe o JSON e converte pra Objeto*/

		if (produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {
			throw new ExceptionLojaVirtual("Tipo da unidade deve ser informada");
		}
		
		if (produto.getDescricao().length() < 10) {
			throw new ExceptionLojaVirtual("Nome do produto deve ter mais de 10 letras.");
		}
		
		if (produto.getEmpresaId() == null || produto.getEmpresaId().getId() <= 0) {
			throw new ExceptionLojaVirtual("Fornecedor deve ser informado");
		}

		if (produto.getId() == null) {
		  List<Produto> produtos  = produtoRepository.buscarProdutoDescricao(produto.getDescricao().toUpperCase(), produto.getEmpresaId().getId());
		  
		  if (!produtos.isEmpty()) {
			  throw new ExceptionLojaVirtual("Já existe Produto com a descrição: " + produto.getDescricao());
		  }
		}
		
		if (produto.getCategoriaId() == null || produto.getCategoriaId().getId() <= 0) {
			throw new ExceptionLojaVirtual("Categoria deve ser informada");
		}		
		
		if (produto.getMarcaId() == null || produto.getMarcaId().getId() <= 0) {
			throw new ExceptionLojaVirtual("Marca deve ser informada");
		}
		
		if (produto.getQtdeEstoque() < 1) {
			throw new ExceptionLojaVirtual("O produto dever ter no minímo 1 no estoque.");
		}
		
		if (produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0) {
			throw new ExceptionLojaVirtual("Deve ser informado imagens para o produto.");
		}
		
		if (produto.getImagens().size() < 3) {
			throw new ExceptionLojaVirtual("Deve ser informado pelo menos 3 imagens para o produto.");
		}
		
		if (produto.getImagens().size() > 6) {
			throw new ExceptionLojaVirtual("Deve ser informado no máximo 6 imagens.");
		}

		if (produto.getId() == null) {
			
			for (int x = 0; x < produto.getImagens().size(); x++) {
				produto.getImagens().get(x).setProduto(produto);
				produto.getImagens().get(x).setEmpresaId(produto.getEmpresaId());
				
				String base64Image = "";
				
				if (produto.getImagens().get(x).getImagemOriginal().contains("data:image")) {
					base64Image = produto.getImagens().get(x).getImagemOriginal().split(",")[1];
				}else {
					base64Image = produto.getImagens().get(x).getImagemOriginal();
				}
				
				byte[] imageBytes =  DatatypeConverter.parseBase64Binary(base64Image);
				
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
				
				if (bufferedImage != null) {
					
					int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
					int largura = Integer.parseInt("800");
					int altura = Integer.parseInt("600");
					
					BufferedImage resizedImage = new BufferedImage(largura, altura, type);
					Graphics2D g = resizedImage.createGraphics();
					g.drawImage(bufferedImage, 0, 0, largura, altura, null);
					g.dispose();
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(resizedImage, "png", baos);
					
					String miniImgBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
					
					produto.getImagens().get(x).setImagemMiniatura(miniImgBase64);
					
					bufferedImage.flush();
					resizedImage.flush();
					baos.flush();
					baos.close();
					
				}
			}
		}		
		
		Produto produtoSalvo = produtoRepository.save(produto);
		
		if (produto.getAlertaQtdeEstoque() && produto.getQtdeEstoque() <= 1) {
			
			StringBuilder html = new StringBuilder();
			html.append("<h2>")
			.append("Produto: " + produto.getDescricao())
			.append(" com estoque baixo: " + produto.getQtdeEstoque());
			html.append("<p> Id Prod.:").append(produto.getId()).append("</p>");
			
			if (produto.getEmpresaId().getEmail() != null) {
				serviceSendEmail.enviarEmailHtml("Produto sem estoque" , html.toString(), produto.getEmpresaId().getEmail());
			}
		}
		
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/deleteProduto") /*Mapeando a url para receber JSON*/
	public ResponseEntity<String> deleteProduto(@RequestBody Produto produto) { /*Recebe o JSON e converte pra Objeto*/
		
		produtoRepository.deleteById(produto.getId());
		
		return new ResponseEntity<String>("Produto Removido",HttpStatus.OK);
	}
	
	//@Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })
	@ResponseBody
	@DeleteMapping(value = "**/deleteProdutoPorId/{id}")
	public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id) { 
		
		produtoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Produto Removido",HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/obterProduto/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionLojaVirtual { 
		
		Produto produto = produtoRepository.findById(id).orElse(null);
		
		if (produto == null) {
			throw new ExceptionLojaVirtual("Não encontrou Produto com código: " + id);
		}
		
		return new ResponseEntity<Produto>(produto,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarProdutoNome/{descricao}")
	public ResponseEntity<List<Produto>> buscarProdutoNome(@PathVariable("descricao") String descricao) { 
		
		List<Produto> acesso = produtoRepository.buscarProdutoDescricao(descricao.toUpperCase());
		
		return new ResponseEntity<List<Produto>>(acesso,HttpStatus.OK);
	}
	
}
