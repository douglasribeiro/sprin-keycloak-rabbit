package com.system.imobiliaria.cartao.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.system.imobiliaria.cartao.domain.ClienteCartao;
import com.system.imobiliaria.cartao.repository.ClienteCartaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

	private final ClienteCartaoRepository repository;
	
	public List<ClienteCartao> listCartaoByCpf(String cpf){
		return repository.findByCpf(cpf);
	}
	
}
