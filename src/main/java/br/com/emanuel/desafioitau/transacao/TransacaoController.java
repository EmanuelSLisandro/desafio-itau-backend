package br.com.emanuel.desafioitau.transacao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@RestController
@RequestMapping(
        value = "transacao",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Slf4j
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoRepository transacaoRepository;

    @PostMapping
    public ResponseEntity<Void> adicionar(@RequestBody TransacaoRequest transacaoRequest) {
        log.info("Adicionando transacao");
        try {
            validarTransacao(transacaoRequest);
            transacaoRepository.add(transacaoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException illegalArgumentException) {
            log.warn("Falha na validação: {}", illegalArgumentException.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            log.error("Erro inesperado ao adicionar transação", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private void validarTransacao(TransacaoRequest transacaoRequest) {
        if (transacaoRequest.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor da transacao invalido!");
        }
        if (transacaoRequest.getDataHora().isAfter(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Data da transacao invalida!");
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> limpar() {
        log.info("Limpando Transacoes");
        transacaoRepository.limpar();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}