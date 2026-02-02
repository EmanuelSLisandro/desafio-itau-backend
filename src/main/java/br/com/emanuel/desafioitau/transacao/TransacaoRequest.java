package br.com.emanuel.desafioitau.transacao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransacaoRequest {
    private BigDecimal valor;
    private OffsetDateTime dataHora;
}
