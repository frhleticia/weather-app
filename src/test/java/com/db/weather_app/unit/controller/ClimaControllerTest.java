package com.db.weather_app.unit.controller;

import com.db.weather_app.controller.ClimaController;
import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.ClimaResponse;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.enums.Tempo;
import com.db.weather_app.exceptions.ClimaNotFoundException;
import com.db.weather_app.exceptions.DuplicidadeClimaException;
import com.db.weather_app.service.ClimaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ClimaControllerTest {

    @InjectMocks
    private ClimaController controller;

    @Mock
    private ClimaService service;

    @Test
    void deveCriarClima() {
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

        var response = new ClimaResponse(
                1L,
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

        when(service.criarClima(request)).thenReturn(response);

        ResponseEntity<ClimaResponse> resultado = controller.criarClima(request);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals("Porto Alegre", resultado.getBody().nomeCidade());

        verify(service).criarClima(request);
    }

    @Test
    void deveAtualizarClima() {
        var request = new AtualizarClimaRequest(
                null,
                null,
                null,
                null,
                null,
                35,
                null,
                null,
                15.0
        );

        var response = new ClimaResponse(
                1L,
                "Porto Alegre",
                LocalDate.of(2026, 2, 20),
                Tempo.SOL,
                Tempo.NUBLADO,
                50,
                35,
                3.0,
                20,
                15.0
        );

        when(service.atualizarClima(1L, request)).thenReturn(response);

        ResponseEntity<ClimaResponse> resultado = controller.atualizarClima(1L, request);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Porto Alegre", resultado.getBody().nomeCidade());
        assertEquals(35, resultado.getBody().temperaturaMin());
        assertEquals(15.0, resultado.getBody().velocidadeVento());

        verify(service).atualizarClima(1L, request);
    }

    @Test
    void deveListarClimas() {
        var response = new ClimaResponse(
                1L,
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

        Pageable pageable = PageRequest.of(0, 8, Sort.by("data").descending());
        Page<ClimaResponse> page = new PageImpl<>(List.of(response));

        when(service.listarClimas(pageable)).thenReturn(page);

        ResponseEntity<Page<ClimaResponse>> resultado = controller.listarClimas(pageable);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1, resultado.getBody().getTotalElements());
        assertEquals(40, resultado.getBody().getContent().getFirst().temperaturaMin());

        verify(service).listarClimas(pageable);
    }

    @Test
    void deveBuscarClimaPorId() {
        var response = new ClimaResponse(
                1L,
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

        when(service.buscarClimaPorId(1L)).thenReturn(response);

        ResponseEntity<ClimaResponse> resultado = controller.buscarClimaPorId(1L);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Porto Alegre", resultado.getBody().nomeCidade());

        verify(service).buscarClimaPorId(1L);
    }

    @Test
    void deveListarClimasPorCidade() {
        var response = new ClimaResponse(
                1L,
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

        Pageable pageable = PageRequest.of(0, 6, Sort.by("data").descending());
        Page<ClimaResponse> page = new PageImpl<>(List.of(response));

        when(service.buscarClimasPorCidade("Porto Alegre", pageable)).thenReturn(page);

        ResponseEntity<Page<ClimaResponse>> resultado = controller.listarClimasPorCidade("Porto Alegre", pageable);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1, resultado.getBody().getTotalElements());
        assertEquals("Porto Alegre", resultado.getBody().getContent().getFirst().nomeCidade());

        verify(service).buscarClimasPorCidade("Porto Alegre", pageable);
    }

    @Test
    void deveBuscarPrevisaoDoDiaPorCidade() {
        var response = new ClimaResponse(
                1L,
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

        when(service.buscarPrevisaoDoDiaPorCidade("Porto Alegre")).thenReturn(response);

        ResponseEntity<ClimaResponse> resultado = controller.buscarPrevisaoDoDiaPorCidade("Porto Alegre");

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Porto Alegre", resultado.getBody().nomeCidade());

        verify(service).buscarPrevisaoDoDiaPorCidade("Porto Alegre");
    }

    @Test
    void deveBuscarPrevisaoProximosSeteDiasPorCidade() {
        var responseDia20 = new ClimaResponse(
                1L,
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

        var responseDia21 = new ClimaResponse(
                1L,
                "Porto Alegre",
                LocalDate.of(2026, 2, 21),
                Tempo.SOL,
                Tempo.NUBLADO,
                45,
                30,
                2.0,
                20,
                5.0
        );

        when(service.buscarPrevisaoProximosSeteDiasPorCidade("Porto Alegre", 7)).thenReturn(List.of(responseDia20, responseDia21));

        ResponseEntity<List<ClimaResponse>> resultado = controller.buscarPrevisaoProximosSeteDiasPorCidade("Porto Alegre", 7);

        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(2, resultado.getBody().size());
        assertEquals("Porto Alegre", resultado.getBody().getFirst().nomeCidade());
        assertEquals(40, resultado.getBody().getFirst().temperaturaMin());
        assertEquals(30, resultado.getBody().get(1).temperaturaMin());

        verify(service).buscarPrevisaoProximosSeteDiasPorCidade("Porto Alegre", 7);
    }

    @Test
    void deveDeletarClima() {
        ResponseEntity<Void> resultado = controller.deletarClima(1L);

        verify(service).deletarClima(1L);
        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());
    }

    @Test
    void deveRetornarNotFoundQuandoClimaNaoExistir() {
        when(service.buscarClimaPorId(999L)).thenThrow(new ClimaNotFoundException("Clima não encontrado"));

        assertThatThrownBy(() -> controller.buscarClimaPorId(999L))
                .isInstanceOf(ClimaNotFoundException.class)
                .hasMessage("Clima não encontrado");

        verify(service).buscarClimaPorId(999L);
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarPrevisaoDoDiaPorCidadeInexistente() {
        when(service.buscarPrevisaoDoDiaPorCidade("Cidade Inexistente"))
                .thenThrow(new ClimaNotFoundException("Previsão do dia não encontrada"));

        assertThatThrownBy(() -> controller.buscarPrevisaoDoDiaPorCidade("Cidade Inexistente"))
                .isInstanceOf(ClimaNotFoundException.class)
                .hasMessage("Previsão do dia não encontrada");

        verify(service).buscarPrevisaoDoDiaPorCidade("Cidade Inexistente");
    }

    @Test
    void deveRetornarNotFoundQuandoListarClimasPorCidadeInexistente() {
        Pageable pageable = PageRequest.of(0, 6, Sort.by("data").descending());
        when(service.buscarClimasPorCidade("Cidade Inexistente", pageable))
                .thenThrow(new ClimaNotFoundException("Nenhum clima encontrado"));

        assertThatThrownBy(() -> controller.listarClimasPorCidade("Cidade Inexistente", pageable))
                .isInstanceOf(ClimaNotFoundException.class)
                .hasMessage("Nenhum clima encontrado");

        verify(service).buscarClimasPorCidade("Cidade Inexistente", pageable);
    }

    @Test
    void deveRetornarNotFoundQuandoListarClimas() {
        Pageable pageable = PageRequest.of(0, 8, Sort.by("data").descending());
        when(service.listarClimas(pageable))
                .thenThrow(new ClimaNotFoundException("Nenhum clima encontrado"));

        assertThatThrownBy(() -> controller.listarClimas(pageable))
                .isInstanceOf(ClimaNotFoundException.class)
                .hasMessage("Nenhum clima encontrado");

        verify(service).listarClimas(pageable);
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarClimaPorIdInexistente() {
        when(service.buscarClimaPorId(999L))
                .thenThrow(new ClimaNotFoundException("Clima não encontrado"));

        assertThatThrownBy(() -> controller.buscarClimaPorId(999L))
                .isInstanceOf(ClimaNotFoundException.class)
                .hasMessage("Clima não encontrado");

        verify(service).buscarClimaPorId(999L);
    }

    @Test
    void deveRetornarNotFoundQuandoAtualizarClimaInexistente() {
        var request = new AtualizarClimaRequest(
                null,
                null,
                null,
                null,
                null,
                35,
                null,
                null,
                15.0
        );

        when(service.atualizarClima(999L, request))
                .thenThrow(new ClimaNotFoundException("Clima não encontrado"));

        assertThatThrownBy(() -> controller.atualizarClima(999L, request))
                .isInstanceOf(ClimaNotFoundException.class)
                .hasMessage("Clima não encontrado");

        verify(service).atualizarClima(999L, request);
    }

    @Test
    void deveRetornarNotFoundQuandoDeletarClimaInexistente() {
        doThrow(new ClimaNotFoundException("Clima não encontrado")).when(service).deletarClima(999L);

        assertThatThrownBy(() -> controller.deletarClima(999L))
                .isInstanceOf(ClimaNotFoundException.class)
                .hasMessage("Clima não encontrado");

        verify(service).deletarClima(999L);
    }

    @Test
    void deveRetornarDataIntegrityViolationQuandoCriarClimaComDadosDuplicados() {
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

        when(service.criarClima(request)).thenThrow(new DuplicidadeClimaException("Registro duplicado"));

        assertThatThrownBy(() -> controller.criarClima(request))
                .isInstanceOf(DuplicidadeClimaException.class)
                .hasMessage("Registro duplicado");

        verify(service).criarClima(request);
    }
}