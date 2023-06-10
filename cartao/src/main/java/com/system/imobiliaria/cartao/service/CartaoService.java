package com.system.imobiliaria.cartao.service;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.system.imobiliaria.cartao.domain.Cartao;
import com.system.imobiliaria.cartao.repository.CartaoRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CartaoService {
	
	private final CartaoRepository repository;
	
	@Transactional
	public Cartao save(Cartao cartao) {
		return repository.save(cartao);
	}

	public List<Cartao> getCartaoRendaMenorIgual(Long renda){
		var rendaBigDecimal = BigDecimal.valueOf(renda);
		return repository.findByRendaLessThanEqual(rendaBigDecimal);
	}
	
	
}
