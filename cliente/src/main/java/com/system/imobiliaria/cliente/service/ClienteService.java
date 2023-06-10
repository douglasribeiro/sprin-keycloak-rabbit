package com.system.imobiliaria.cliente.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.system.imobiliaria.cliente.model.Cliente;
import com.system.imobiliaria.cliente.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ClienteService {

	private final ClienteRepository clienteRepository;
	
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	public Optional<Cliente> getByCPF(String cpf){
		return clienteRepository.findByCpf(cpf);
	}
	
	public Optional<Cliente> getByEmail(String email){
		return clienteRepository.findByEmail(email);
	}
	
}
