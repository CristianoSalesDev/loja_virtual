package com.br.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
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
	
}
