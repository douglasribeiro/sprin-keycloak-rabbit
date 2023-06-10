package com.system.imobiliaria.cliente.model;

import lombok.Data;

@Data
public class ClienteSaveRequest {
	
	private String email;
	
	private String cpf;
	
	private String nome;
	
	private Integer idade;
	
	public Cliente toModel() {
		return new Cliente(email, cpf, nome, idade);
	}

}
