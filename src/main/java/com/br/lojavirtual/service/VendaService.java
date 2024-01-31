package com.br.lojavirtual.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.br.lojavirtual.model.ItemPedido;
import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.model.dto.ItemPedidoDto;
import com.br.lojavirtual.model.dto.PedidoDTO;
import com.br.lojavirtual.repository.PedidoRepository;

@Service
public class VendaService {

	@PersistenceContext
	private EntityManager entityManager;

	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PedidoRepository pedidorepository;

	public void exclusaoTotalVendaBanco2(Long idVenda) {
		String sql = "begin; update t_pedido set excluido = true where id = " + idVenda +"; commit;";
		jdbcTemplate.execute(sql);;
	}	

    public void exclusaoTotalVendaBanco(Long idVenda) {
		
		String value = 
		                  " begin;"
		      			+ " UPDATE t_nfe set pedido_id = null where pedido_id = "+idVenda+"; "
		      			+ " delete from t_nfe where pedido_id = "+idVenda+"; "
		      			+ " delete from t_item_pedido where pedido_id = "+idVenda+"; "
		      			+ " delete from status_rastreio where pedido_id = "+idVenda+"; "
		      			+ " delete from t_pedido where id = "+idVenda+"; "
		      			+ " commit; ";
		
		jdbcTemplate.execute(value);
	}

	public void ativaRegistroVendaBanco(Long idVenda) {
		String sql = "begin; update t_pedido set excluido = false where id = " + idVenda +"; commit;";
		jdbcTemplate.execute(sql);;
		
	}
	
	/*HQL (Hibernate) ou JPQL (JPA ou Spring Data)*/
	public List<Pedido> consultaVendaPorData(String data1, String data2) throws ParseException{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date1 = dateFormat.parse(data1);
		Date date2 = dateFormat.parse(data2);
		
		
		return pedidorepository.consultaVendaPorData(date1, date2);
		
	}
	
	
	public PedidoDTO consultaVenda(Pedido pedido) {
		

		PedidoDTO pedidoDTO = new PedidoDTO();

		pedidoDTO.setValorTotal(pedido.getValorTotal());
		pedidoDTO.setPessoa(pedido.getPessoa());

		pedidoDTO.setEntrega(pedido.getEnderecoEntrega());
		pedidoDTO.setCobranca(pedido.getEnderecoCobranca());

		pedidoDTO.setValorDesconto(pedido.getValorDesconto());
		pedidoDTO.setValorFrete(pedido.getValorFrete());
		pedidoDTO.setId(pedido.getId());

		for (ItemPedido item : pedido.getItemPedidoId()) {

			ItemPedidoDto itemPedidoDTO = new ItemPedidoDto();
			itemPedidoDTO.setQuantidade(item.getQuantidade());
			itemPedidoDTO.setProduto(item.getProdutoId());

			pedidoDTO.getItemPedido().add(itemPedidoDTO);
		}
		
		return pedidoDTO;
	}	
}
