package com.db.weather_app.unit.mapper;

import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.entity.Clima;
import com.db.weather_app.enums.Tempo;
import com.db.weather_app.mapper.ClimaMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClimaMapperTest {

    private final ClimaMapper mapper = new ClimaMapper();

    private Clima criarClimaCompleto() {
        return Clima.builder()
                .id(1L)
                .nomeCidade("Porto Alegre")
                .data(LocalDate.now())
                .tempoDia(Tempo.SOL)
                .tempoNoite(Tempo.NUBLADO)
                .temperaturaMax(30)
                .temperaturaMin(20)
                .precipitacao(5.0)
                .humidade(70)
                .velocidadeVento(10.0)
                .build();
    }

    @Test
    void deveRetornarClimaEntityQuandoReceberCriarClimaRequest() {
        var request = new CriarClimaRequest(
                "Porto Alegre",
                LocalDate.of(2026, 2, 20),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        );

        Clima entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("Porto Alegre", entity.getNomeCidade());
        assertEquals(50, entity.getTemperaturaMax());
        assertEquals(20, entity.getHumidade());
    }

    @Test
    void deveRetornarClimaResponseQuandoReceberClimaEntity() {
        var clima = criarClimaCompleto();

        var response = mapper.toResponse(clima);

        assertNotNull(response);
        assertEquals(clima.getId(), response.id());
        assertEquals(clima.getNomeCidade(), response.nomeCidade());
        assertEquals(clima.getTemperaturaMax(), response.temperaturaMax());
    }

    @Test
    void deveAtualizarClimaEntityQuandoReceberAtualizarClimaRequest() {
        var clima = criarClimaCompleto();

        var request = new AtualizarClimaRequest(
                "Porto Alegre",
                LocalDate.of(2026, 2, 20),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                40,
                3.0,
                20,
                10.0
        );

        mapper.updateEntity(clima, request);

        assertEquals("Porto Alegre", clima.getNomeCidade());
        assertEquals(50, clima.getTemperaturaMax());
        assertEquals(20, clima.getHumidade());
    }

    @Test
    void deveManterValoresInalteradosQuandoAtualizarClimaEntityComCamposNulos() {
        var clima = criarClimaCompleto();

        var request = new AtualizarClimaRequest(
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

        mapper.updateEntity(clima, request);

        assertEquals("Porto Alegre", clima.getNomeCidade());
        assertEquals(30, clima.getTemperaturaMax());
        assertEquals(70, clima.getHumidade());
    }

    @Test
    void deveRetornarClimaResponseListQuandoReceberClimaEntityList() {
        var clima1 = criarClimaCompleto();
        var clima2 = criarClimaCompleto();
        clima2.setId(2L);
        clima2.setNomeCidade("São Paulo");

        var climas = List.of(clima1, clima2);

        var responses = mapper.toResponseList(climas);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Porto Alegre", responses.get(0).nomeCidade());
        assertEquals("São Paulo", responses.get(1).nomeCidade());
    }
}
