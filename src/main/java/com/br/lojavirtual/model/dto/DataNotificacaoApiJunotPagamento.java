package com.br.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*Objeto principal recebimento api juno boleto pix - webhook*/
public class DataNotificacaoApiJunotPagamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String eventId;
	private String eventType;
	private String timestamp;

	private List<AttributesNotificacaoPagaApiJuno> data = new ArrayList<AttributesNotificacaoPagaApiJuno>();

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<AttributesNotificacaoPagaApiJuno> getData() {
		return data;
	}

	public void setData(List<AttributesNotificacaoPagaApiJuno> data) {
		this.data = data;
	}	

}
