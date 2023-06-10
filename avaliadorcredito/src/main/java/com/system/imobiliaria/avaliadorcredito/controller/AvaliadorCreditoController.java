package com.system.imobiliaria.avaliadorcredito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.system.imobiliaria.avaliadorcredito.domain.DadosAvaliacao;
import com.system.imobiliaria.avaliadorcredito.domain.DadosSolicitacaoEmissaoCartao;
import com.system.imobiliaria.avaliadorcredito.domain.ProtocoloSolicitacaoCartao;
import com.system.imobiliaria.avaliadorcredito.domain.RetornoAvaliacaoCliente;
import com.system.imobiliaria.avaliadorcredito.exception.DadosClienteNotFoundException;
import com.system.imobiliaria.avaliadorcredito.exception.ErroComunicacaoMicroServiceException;
import com.system.imobiliaria.avaliadorcredito.exception.ErroSolicitacaoCartaoException;
import com.system.imobiliaria.avaliadorcredito.service.AvaliadorCreditoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("avaliacoes-credito")
@Slf4j
public class AvaliadorCreditoController {
	
	@Autowired
	private AvaliadorCreditoService avaliadorCreditoService;
	
	@GetMapping
	public String status() {
		log.info("Avaliador de credito esta ok!");
		avaliadorCreditoService.servicosOk();
		return "Ok!.";
	}
	
	@GetMapping(value = "situacao-cliente", params = "cpf")
	public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf){
		try {
			var situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
			return ResponseEntity.ok(situacaoCliente);
		} catch (DadosClienteNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(ErroComunicacaoMicroServiceException e) {
			return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
		}
		
	}

	@PostMapping
	public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
		try {
		    RetornoAvaliacaoCliente retornoAvaliacaoCliente =  avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
		    return ResponseEntity.ok(retornoAvaliacaoCliente);
		} catch (DadosClienteNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch(ErroComunicacaoMicroServiceException e) {
			return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
		}	
	}
	
	@PostMapping("solicitacoes-cartao")
	public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
		log.info("Solicitação de cartoes.");
		try {
			ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService.solicitarEmissaoCartao(dados);
			log.info("Solicitação de cartoes final.");
			return ResponseEntity.ok(protocoloSolicitacaoCartao);
		} catch (ErroSolicitacaoCartaoException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
