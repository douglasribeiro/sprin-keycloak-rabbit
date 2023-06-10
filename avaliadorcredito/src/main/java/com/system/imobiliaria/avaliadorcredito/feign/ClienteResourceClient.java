package com.system.imobiliaria.avaliadorcredito.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.system.imobiliaria.avaliadorcredito.domain.DadosCliente;

@FeignClient(value = "cliente", path = "/cliente")
public interface ClienteResourceClient {

	@GetMapping(params = "cpf")
	ResponseEntity<DadosCliente> dadosCliente(@RequestParam("cpf") String cpf);
	
	@GetMapping
	public String status();
}
