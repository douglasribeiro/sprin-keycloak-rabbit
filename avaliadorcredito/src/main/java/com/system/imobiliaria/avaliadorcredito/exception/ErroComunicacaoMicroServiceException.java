package com.system.imobiliaria.avaliadorcredito.exception;

import lombok.Getter;

public class ErroComunicacaoMicroServiceException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	private Integer status;

	public ErroComunicacaoMicroServiceException(String msg, Integer status) {
		super(msg);
		this.status = status;
	}

}
