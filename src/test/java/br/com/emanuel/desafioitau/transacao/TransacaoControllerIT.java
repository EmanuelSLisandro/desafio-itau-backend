package br.com.emanuel.desafioitau.transacao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransacaoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCriarTransacaoValida() throws Exception{
        String json = """
            {
              "valor": 100.0,
              "dataHora": "2024-01-01T10:00:00-03:00"
            }
            """;

        mockMvc.perform(post("/transacao").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }

    @Test
    void deveRetornar422QuandoValorForNegativo() throws Exception {
        String json = """
        {
          "valor": -10.0,
          "dataHora": "2024-01-01T10:00:00-03:00"
        }
        """;

        mockMvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar422QuandoDataForFutura() throws Exception {
        String json = """
        {
          "valor": 10.0,
          "dataHora": "2999-01-01T10:00:00-03:00"
        }
        """;

        mockMvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity());
    }
}
