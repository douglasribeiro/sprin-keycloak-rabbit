package com.system.imobiliaria.cliente.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.system.imobiliaria.cliente.model.Cliente;
import com.system.imobiliaria.cliente.model.ClienteSaveRequest;
import com.system.imobiliaria.cliente.service.ClienteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("cliente")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ClienteController {
	
	private final ClienteService service;
	
	@GetMapping
	public String status() {
		log.info("Teste requisição cliente, Ok!");
		return "Ok!.";
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody ClienteSaveRequest request) {
		log.info("Salvar Cliente.");
		Cliente cliente = request.toModel();
		service.save(cliente);
		URI headerLocation = ServletUriComponentsBuilder.fromCurrentRequest()
				.query("cpf={cpf}")
				.buildAndExpand(cliente.getCpf()).toUri(); 
		return ResponseEntity.created(headerLocation).build();
	}

	@GetMapping(params = "cpf")
	public ResponseEntity pesquisaCpf(@RequestParam("cpf") String cpf){
		log.info("Pesuisa cliente por CPF.");
		var cliente = service.getByCPF(cpf);
		if(cliente.isEmpty())
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(cliente);
	}
	
	@GetMapping(params = "email")
	public ResponseEntity pesquisaEmail(@RequestParam("email") String email){
		log.info("Pesuisa cliente por Email.");
		var cliente = service.getByEmail(email);
		if(cliente.isEmpty())
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(cliente);
	}
	
}
