package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class ReplyTo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fromFieldId;


	 // Getter Methods 

	  public String getFromFieldId() {
	    return fromFieldId;
	  }

	 // Setter Methods 

	  public void setFromFieldId( String fromFieldId ) {
	    this.fromFieldId = fromFieldId;
	  }	

}
