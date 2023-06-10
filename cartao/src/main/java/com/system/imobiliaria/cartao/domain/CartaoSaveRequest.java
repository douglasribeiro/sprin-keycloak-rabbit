package com.system.imobiliaria.cartao.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartaoSaveRequest {
	
	private String nome;
	private BandeiraCartao bandeira;
	private BigDecimal renda;
	private BigDecimal limite;

	public Cartao toModel() {
		return new Cartao(nome, bandeira, renda, limite);
	}
}
