package com.system.imobiliaria.cartao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.imobiliaria.cartao.domain.ClienteCartao;

public interface ClienteCartaoRepository extends JpaRepository<ClienteCartao, Long>{
	
	List<ClienteCartao> findByCpf(String cpf);

}
