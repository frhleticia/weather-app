package com.db.weather_app.integration;

import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.entity.Clima;
import com.db.weather_app.enums.Tempo;
import com.db.weather_app.repository.ClimaRepository;
import com.db.weather_app.service.ClimaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClimaControllerIT {

    @Autowired
    private ClimaRepository repository;

    @Autowired
    private ClimaService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void deveCriarClimaQuandoDadosValidos() throws Exception {
        var request = new CriarClimaRequest(
                "Porto Alegre",
                LocalDate.now(),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        );

        String climaRequestJson = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/dados-meteorologicos/registrar-cidade")
                                .contentType("application/json")
                                .content(climaRequestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nomeCidade").value("Porto Alegre"))
                .andExpect(jsonPath("$.temperaturaMax").value(50));
    }

    @Test
    void deveRetornarErroQuandoRegistrarClimaDuplicado() throws Exception {
        var request = new CriarClimaRequest(
                "Porto Alegre",
                LocalDate.now(),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        );

        service.criarClima(request);

        String climaRequestJson = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/dados-meteorologicos/registrar-cidade")
                                .contentType("application/json")
                                .content(climaRequestJson)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void deveRetornarErroQuandoCriarClimaComDadosInvalidos() throws Exception {
        var request = new CriarClimaRequest(
                "",
                LocalDate.now(),
                Tempo.SOL,
                Tempo.NUBLADO,
                -10,
                40,
                3.0,
                20,
                10.0
        );

        String climaRequestJson = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/dados-meteorologicos/registrar-cidade")
                                .contentType("application/json")
                                .content(climaRequestJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarCamposNaoNulosComSucesso() throws Exception {
        var request = new CriarClimaRequest(
                "Porto Alegre",
                LocalDate.now(),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        );

        var climaResponse = service.criarClima(request);

        var atualizarRequest = new AtualizarClimaRequest(
                "São Paulo",
                null,
                Tempo.CHUVA,
                Tempo.NUBLADO,
                55,
                30,
                1.0,
                30,
                5.0
        );

        String atualizarRequestJson = mapper.writeValueAsString(atualizarRequest);

        mockMvc.perform(
                        patch("/dados-meteorologicos/" + climaResponse.id() + "/atualizar-registro")
                                .contentType("application/json")
                                .content(atualizarRequestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(climaResponse.id()))
                .andExpect(jsonPath("$.temperaturaMax").value(55))
                .andExpect(jsonPath("$.precipitacao").value(1.0));
    }

    @Test
    void deveManterValoresInalteradosQuandoAtualizarComCamposNulos() throws Exception {
        var request = new CriarClimaRequest(
                "Porto Alegre",
                LocalDate.now(),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        );

        var climaResponse = service.criarClima(request);

        var atualizarRequest = new AtualizarClimaRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        String atualizarRequestJson = mapper.writeValueAsString(atualizarRequest);

        mockMvc.perform(
                        patch("/dados-meteorologicos/" + climaResponse.id() + "/atualizar-registro")
                                .contentType("application/json")
                                .content(atualizarRequestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(climaResponse.id()))
                .andExpect(jsonPath("$.temperaturaMax").value(50))
                .andExpect(jsonPath("$.temperaturaMin").value(40));
    }

    @Test
    void deveRetornarErroQuandoAtualizarClimaInexistente() throws Exception {
        var atualizarRequest = new AtualizarClimaRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        String atualizarRequestJson = mapper.writeValueAsString(atualizarRequest);

        mockMvc.perform(
                        patch("/dados-meteorologicos/999/atualizar-registro")
                                .contentType("application/json")
                                .content(atualizarRequestJson)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarListaDeClimasQuandoExistiremRegistros() throws Exception {
        repository.saveAll(List.of(
                new Clima(null, "Porto Alegre", LocalDate.now(), Tempo.SOL, Tempo.NUBLADO, 50,40,3.0,20,10.0),
                new Clima(null, "Porto Alegre", LocalDate.now().plusDays(1), Tempo.CHUVA, Tempo.NUBLADO, 30,20,5.0,80,15.0)
        ));

        mockMvc.perform(get("/dados-meteorologicos")
                .param("page", "0")
                .param("size", "8")
                .param("sort", "data,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].precipitacao").value(5.0));
    }

    @Test
    void deveBuscarClimaPorIdQuandoRegistroExistir() throws Exception {
        var request = new CriarClimaRequest(
                "Porto Alegre",
                LocalDate.now(),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        );

        var climaResponse = service.criarClima(request);

        mockMvc.perform(get("/dados-meteorologicos/" + climaResponse.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(climaResponse.id()))
                .andExpect(jsonPath("$.nomeCidade").value("Porto Alegre"));
    }

    @Test
    void deveRetornarErroQuandoBuscarClimaPorIdInexistente() throws Exception {
        mockMvc.perform(get("/dados-meteorologicos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarClimasPorCidadeQuandoExistiremRegistros() throws Exception {
        repository.saveAll(List.of(
                new Clima(null, "Porto Alegre", LocalDate.now(), Tempo.SOL, Tempo.NUBLADO, 50,40,3.0,20,10.0),
                new Clima(null, "Porto Alegre", LocalDate.now().plusDays(1), Tempo.CHUVA, Tempo.NUBLADO, 30,20,5.0,80,15.0),
                new Clima(null, "São Paulo", LocalDate.now(), Tempo.SOL, Tempo.NUBLADO, 25,15,2.0,60,5.0)
        ));

        mockMvc.perform(get("/dados-meteorologicos/cidade/Porto Alegre")
                .param("page", "0")
                .param("size", "6")
                .param("sort", "data,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nomeCidade").value("Porto Alegre"));
    }

    @Test
    void deveRetornarListaVaziaQuandoBuscarClimasPorCidadeSemRegistros() throws Exception {
        mockMvc.perform(get("/dados-meteorologicos/cidade/Curitiba")
                .param("page", "0")
                .param("size", "6")
                .param("sort", "data,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void deveBuscarPrevisaoDoDiaPorCidadeQuandoRegistroExistir() throws Exception {
        repository.save(new Clima(
                null,
                "Porto Alegre",
                LocalDate.now(),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        ));

        mockMvc.perform(get("/dados-meteorologicos/cidade/Porto Alegre/previsao-dia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nomeCidade").value("Porto Alegre"))
                .andExpect(jsonPath("$.data").value(LocalDate.now().toString()));
    }

    @Test
    void deveRetornarErroQuandoBuscarPrevisaoDoDiaPorCidadeSemRegistro() throws Exception {
        mockMvc.perform(get("/dados-meteorologicos/cidade/Curitiba/previsao-dia"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarPrevisaoProximosSeteDiasPorCidadeQuandoExistiremRegistros() throws Exception {
        repository.saveAll(List.of(
                new Clima(null, "Porto Alegre", LocalDate.now(), Tempo.SOL, Tempo.NUBLADO, 50,40,3.0,20,10.0),
                new Clima(null, "Porto Alegre", LocalDate.now().plusDays(1), Tempo.CHUVA, Tempo.NUBLADO, 30,20,5.0,80,15.0),
                new Clima(null, "Porto Alegre", LocalDate.now().plusDays(2), Tempo.NUBLADO, Tempo.CHUVA, 25,15,2.0,60,5.0)
        ));

        mockMvc.perform(get("/dados-meteorologicos/cidade/Porto Alegre/previsao-proximos-dias")
                .param("dia", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nomeCidade").value("Porto Alegre"));
    }

    @Test
    void deveRetornarListaVaziaQuandoBuscarPrevisaoProximosSeteDiasPorCidadeSemRegistros() throws Exception {
        mockMvc.perform(get("/dados-meteorologicos/cidade/Curitiba/previsao-proximos-dias")
                .param("dia", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deveDeletarClimaQuandoRegistroExistir() throws Exception {
        var request = new CriarClimaRequest(
                "Porto Alegre",
                LocalDate.now(),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        );

        var climaResponse = service.criarClima(request);

        mockMvc.perform(delete("/dados-meteorologicos/" + climaResponse.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarErroQuandoDeletarClimaInexistente() throws Exception {
        mockMvc.perform(delete("/dados-meteorologicos/999"))
                .andExpect(status().isNotFound());
    }
}
