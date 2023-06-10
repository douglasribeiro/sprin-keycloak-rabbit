package com.system.imobiliaria.avaliadorcredito.exception;

public class DadosClienteNotFoundException extends Exception{
	
	public DadosClienteNotFoundException() {
		super("Dados do cliente nao encontrados par o CPF informado.");
	}

}
