package com.br.lojavirtual.model.dto;

import java.io.Serializable;

public class ObjetoMsgGeral implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String msg;
    
    public ObjetoMsgGeral(String msg) {
       this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
    
}
