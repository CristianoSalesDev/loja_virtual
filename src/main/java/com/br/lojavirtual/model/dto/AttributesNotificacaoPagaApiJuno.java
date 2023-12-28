package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class AttributesNotificacaoPagaApiJuno implements Serializable {

	private static final long serialVersionUID = 1L;

	private String entityId;
	private String entityType;

	private AttributesNotificacaoPagaJuno attributes = new AttributesNotificacaoPagaJuno();

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public AttributesNotificacaoPagaJuno getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributesNotificacaoPagaJuno attributes) {
		this.attributes = attributes;
	}
}
