package br.com.emanuel.desafioitau.transacao;

import br.com.emanuel.desafioitau.estatistica.EstatisticaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

@Repository
@Slf4j
public class TransacaoRepository {

    private final List<TransacaoRequest> transacoes = new ArrayList<>();

    public void add(TransacaoRequest transacaoRequest) {
        transacoes.add(transacaoRequest);
        log.info("Transação adicionada. Total de transações: {}", transacoes.size());
    }

    public void limpar() {
        transacoes.clear();
        log.info("Todas as transações foram removidas");
    }

    public EstatisticaDTO estatistica(OffsetDateTime horaInicial) {

        log.info("Calculando estatísticas a partir de {}", horaInicial);
        log.info("Total de transações armazenadas: {}", transacoes.size());

        final var transacoesFiltradas = transacoes.stream()
                .filter(t -> !t.getDataHora().isBefore(horaInicial))
                .toList();

        log.info("Transações dentro do intervalo: {}", transacoesFiltradas.size());

        if (transacoesFiltradas.isEmpty()) {
            log.info("Nenhuma transação encontrada no intervalo. Retornando estatísticas zeradas");
            return new EstatisticaDTO();
        }

        DoubleStream doubleStream = transacoesFiltradas.stream()
                .map(TransacaoRequest::getValor)
                .mapToDouble(BigDecimal::doubleValue);

        log.info("Estatísticas calculadas com sucesso");
        return new EstatisticaDTO(doubleStream.summaryStatistics());
    }
}
