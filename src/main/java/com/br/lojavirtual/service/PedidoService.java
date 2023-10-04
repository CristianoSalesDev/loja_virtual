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

import com.br.lojavirtual.model.Pedido;
import com.br.lojavirtual.repository.PedidoRepository;

@Service
public class PedidoService {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public void exclusaoPedidoExcluido(Long idPedido) {
		String sql = "begin; update t_pedido set excluido = true where id = "+idPedido+"; commit;";
		jdbcTemplate.execute(sql);
	}
	
	public void ativarPedidoExcluido(Long idPedido) {
		String sql = "begin; update t_pedido set excluido = false where id = "+idPedido+"; commit;";
		jdbcTemplate.execute(sql);
	}	

	public void exclusaoPedido(Long idPedido) {
		
		String sqlDeletePedido = 
		                  " begin;"
		      			+ " UPDATE t_nfe set pedido_id = null where pedido_id = "+idPedido+"; "
		      			+ " delete from t_nfe where pedido_id = "+idPedido+"; "
		      			+ " delete from t_item_pedido where pedido_id = "+idPedido+"; "
		      			+ " delete from t_status_rastreio where pedido_id = "+idPedido+"; "
		      			+ " delete from t_pedido where id = "+idPedido+"; "
		      			+ " commit; ";
		
		jdbcTemplate.execute(sqlDeletePedido);
	}

	/*HQL (Hibernate) ou JPQL (JPA ou Spring Data)*/
	public List<Pedido> consultaVendaPorData(String data1, String data2) throws ParseException{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		
		Date date1 = dateFormat.parse(data1);		
		Date date2 = dateFormat.parse(data2);
		
		return pedidoRepository.consultaVendaPorData(date1, date2);
		
	}	
	
}
