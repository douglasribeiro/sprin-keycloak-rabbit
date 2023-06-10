package com.system.imobiliaria.avaliadorcredito.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.system.imobiliaria.avaliadorcredito.domain.Cartao;
import com.system.imobiliaria.avaliadorcredito.domain.CartaoAprovado;
import com.system.imobiliaria.avaliadorcredito.domain.CartaoCliente;
import com.system.imobiliaria.avaliadorcredito.domain.DadosCliente;
import com.system.imobiliaria.avaliadorcredito.domain.DadosSolicitacaoEmissaoCartao;
import com.system.imobiliaria.avaliadorcredito.domain.ProtocoloSolicitacaoCartao;
import com.system.imobiliaria.avaliadorcredito.domain.RetornoAvaliacaoCliente;
import com.system.imobiliaria.avaliadorcredito.domain.SituacaoCliente;
import com.system.imobiliaria.avaliadorcredito.exception.DadosClienteNotFoundException;
import com.system.imobiliaria.avaliadorcredito.exception.ErroComunicacaoMicroServiceException;
import com.system.imobiliaria.avaliadorcredito.exception.ErroSolicitacaoCartaoException;
import com.system.imobiliaria.avaliadorcredito.feign.CartoesResourceClient;
import com.system.imobiliaria.avaliadorcredito.feign.ClienteResourceClient;
import com.system.imobiliaria.avaliadorcredito.mqueue.SolicitacaoEmissaoCartaoPublisher;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvaliadorCreditoService {
	
	private final ClienteResourceClient clientesClient;
	private final CartoesResourceClient cartoesClient;
	private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;
	
	public void servicosOk() {
		var retornoCliente = clientesClient.status();
		log.info("retorno cliente '{}'", retornoCliente);
		var retornoCartao = cartoesClient.status();
		log.info("retorno cartoes '{}'", retornoCartao);
	}

	public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroServiceException {
		
		try {
			ResponseEntity<DadosCliente> dadosClientResponse = clientesClient.dadosCliente(cpf);
			ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);
			
			return SituacaoCliente
					.builder()
					.cliente(dadosClientResponse.getBody())
					.cartoes(cartoesResponse.getBody())
					.build();
		} catch (FeignException.FeignClientException e) {
			int status = e.status();
			if(HttpStatus.NOT_FOUND.value() == status) {
				throw new DadosClienteNotFoundException();
			}
			throw new ErroComunicacaoMicroServiceException(e.getMessage(), status);
		}
		
	}
	
	public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) 
			throws DadosClienteNotFoundException, ErroComunicacaoMicroServiceException {
	
		try {
			ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
			ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAteh(renda);
			
			List<Cartao> cartoes = cartoesResponse.getBody();
			var listaCartoesAprovados = cartoes.stream().map(cartao -> {
				
				DadosCliente dadosCliente = dadosClienteResponse.getBody();
				
				BigDecimal limiteBasico = cartao.getLimiteBasico();
				//BigDecimal rendaBD = BigDecimal.valueOf(renda);
				BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
				var fator = idadeBD.divide(BigDecimal.valueOf(10));
				BigDecimal limiteAprovado = fator.multiply(limiteBasico);
				
				CartaoAprovado aprovado = new CartaoAprovado();
				aprovado.setCartao(cartao.getNome());
				aprovado.setBandeira(cartao.getBandeira());
				aprovado.setLimiteAprovado(limiteAprovado);
				
				return aprovado;
			}).collect(Collectors.toList());
			
			return new RetornoAvaliacaoCliente(listaCartoesAprovados);
			
		} catch (FeignException.FeignClientException e) {
			int status = e.status();
			if(HttpStatus.NOT_FOUND.value() == status) {
				throw new DadosClienteNotFoundException();
			}
			throw new ErroComunicacaoMicroServiceException(e.getMessage(), status);
		}
	}
	
	public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {
		try {
			log.info("Solicitação cartoes inicio.");
			emissaoCartaoPublisher.solicitarCartao(dados);
			var protocolo = UUID.randomUUID().toString();
			log.info("Solicitação cartoes final.");
			return new ProtocoloSolicitacaoCartao(protocolo);
		} catch (Exception e) {
			throw new ErroSolicitacaoCartaoException(e.getMessage());
		}
	}
}
