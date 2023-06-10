package com.system.imobiliaria.avaliadorcredito.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetornoAvaliacaoCliente {

	private List<CartaoAprovado> cartoes;
	
}
