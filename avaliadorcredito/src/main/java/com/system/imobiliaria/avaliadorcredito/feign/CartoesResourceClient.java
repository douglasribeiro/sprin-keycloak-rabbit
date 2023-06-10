package com.system.imobiliaria.avaliadorcredito.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.system.imobiliaria.avaliadorcredito.domain.Cartao;
import com.system.imobiliaria.avaliadorcredito.domain.CartaoCliente;

@FeignClient(value = "cartao", path = "cartao")
public interface CartoesResourceClient {
	
	@GetMapping(params = "cpf")
	ResponseEntity<List<CartaoCliente>> getCartoesByCliente(@RequestParam("cpf") String cpf);
	
	@GetMapping(params = "renda")
	ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda );
	
	@GetMapping
	public String status();

}
