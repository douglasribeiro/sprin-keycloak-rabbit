package com.system.imobiliaria.cartao.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.system.imobiliaria.cartao.domain.Cartao;
import com.system.imobiliaria.cartao.domain.CartaoSaveRequest;
import com.system.imobiliaria.cartao.domain.CartoesPorClienteResponse;
import com.system.imobiliaria.cartao.domain.ClienteCartao;
import com.system.imobiliaria.cartao.service.CartaoService;
import com.system.imobiliaria.cartao.service.ClienteCartaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("cartao")
@Slf4j
@RequiredArgsConstructor
public class CartaoController {
	
	private final CartaoService service;
	private final ClienteCartaoService clienteCartaoService;

	@GetMapping
	public String status() {
		log.info("Serviço de cartao esta ok!.");
		return "Ok!";
	}
	
	@PostMapping
	public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request) {
		Cartao cartao = request.toModel();
		service.save(cartao);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping(params = "renda")
	public ResponseEntity<List<Cartao>> getCartaoRendaAteh(@RequestParam("renda") Long renda){
		List<Cartao> list = service.getCartaoRendaMenorIgual(renda);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping(params = "cpf")
	public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf){
		List<ClienteCartao> lista = clienteCartaoService.listCartaoByCpf(cpf);
		List<CartoesPorClienteResponse> resultList = lista.stream().map(CartoesPorClienteResponse::fromModel)
				.collect(Collectors.toList());
		return ResponseEntity.ok(resultList);
	}
}
