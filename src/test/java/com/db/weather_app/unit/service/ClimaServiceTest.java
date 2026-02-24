package com.db.weather_app.unit.service;

import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.ClimaResponse;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.entity.Clima;
import com.db.weather_app.enums.Tempo;
import com.db.weather_app.exceptions.ClimaNotFoundException;
import com.db.weather_app.mapper.ClimaMapper;
import com.db.weather_app.repository.ClimaRepository;
import com.db.weather_app.service.ClimaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClimaServiceTest {

    @Mock
    private ClimaRepository repository;

    private ClimaService service;

    @BeforeEach
    void setup() {
        var mapper = new ClimaMapper();
        service = new ClimaService(repository, new ClimaMapper());
    }

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
    void deveRegistrarClimaQuandoDadosValidos() {
        var request = new CriarClimaRequest(
                "Porto Alegre",
                LocalDate.of(2026, 2, 20),
                Tempo.SOL,
                Tempo.NUBLADO,
                30,
                20,
                5.0,
                70,
                10.0
        );

        Clima climaSalvo = criarClimaCompleto();

        when(repository.save(any(Clima.class)))
                .thenReturn(climaSalvo);

        ClimaResponse resultado = service.criarClima(request);

        assertNotNull(resultado);
        assertNotNull(resultado.id());
        assertEquals("Porto Alegre", resultado.nomeCidade());

        verify(repository).save(any(Clima.class));
    }

    @Test
    void deveListarClimasQuandoExistiremRegistros() {
        Pageable pageable = PageRequest.of(0, 8);

        Clima clima = criarClimaCompleto();
        //cria manualmente uma página contendo 1 elemento
        Page<Clima> page = new PageImpl<>(List.of(clima));

        when(repository.findByDataGreaterThanEqual(any(), eq(pageable)))
                .thenReturn(page);

        Page<ClimaResponse> resultado = service.listarClimas(pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Porto Alegre", resultado.getContent().getFirst().nomeCidade());

        verify(repository).findByDataGreaterThanEqual(any(), eq(pageable));
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistiremRegistros() {
        Pageable pageable = PageRequest.of(0, 8);
        Page<Clima> pageVazia = new PageImpl<>(List.of());

        when(repository.findByDataGreaterThanEqual(any(), eq(pageable)))
                .thenReturn(pageVazia);

        assertThrows(ClimaNotFoundException.class,
                () -> service.listarClimas(pageable)
        );
    }

    @Test
    void deveBuscarClimaPorIdQuandoRegistroExistir() {
        Clima clima = criarClimaCompleto();

        when(repository.findById(1L))
                .thenReturn(Optional.of(clima));

        ClimaResponse resultado = service.buscarClimaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Porto Alegre", resultado.nomeCidade());

        verify(repository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoBuscarClimaPorIdInexistente() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ClimaNotFoundException.class,
                () -> service.buscarClimaPorId(1L)
        );

        verify(repository).findById(1L);
    }

    @Test
    void deveBuscarClimasPorCidadeQuandoExistiremRegistros() {
        Pageable pageable = PageRequest.of(0, 6);
        Clima clima = criarClimaCompleto();
        Page<Clima> page = new PageImpl<>(List.of(clima));

        when(repository.findByNomeCidadeAndDataAfter(eq("Porto Alegre"), any(), eq(pageable)))
                .thenReturn(page);

        Page<ClimaResponse> resultado = service.buscarClimasPorCidade("Porto Alegre", pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Porto Alegre", resultado.getContent().getFirst().nomeCidade());

        verify(repository).findByNomeCidadeAndDataAfter(eq("Porto Alegre"), any(), eq(pageable));
    }

    @Test
    void deveBuscarPrevisaoDoDiaPorCidadeQuandoRegistroExistir() {
        Clima clima = criarClimaCompleto();

        when(repository.findByNomeCidadeAndData(eq("Porto Alegre"), any()))
                .thenReturn(Optional.of(clima));

        ClimaResponse resultado = service.buscarPrevisaoDoDiaPorCidade("Porto Alegre");

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Porto Alegre", resultado.nomeCidade());

        verify(repository).findByNomeCidadeAndData(eq("Porto Alegre"), any());
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPrevisaoDoDiaPorCidadeInexistente() {
        when(repository.findByNomeCidadeAndData(eq("Porto Alegre"), any()))
                .thenReturn(Optional.empty());

        assertThrows(ClimaNotFoundException.class,
                () -> service.buscarPrevisaoDoDiaPorCidade("Porto Alegre")
        );
    }

    @Test
    void deveBuscarPrevisaoProximosSeteDiasPorCidadeQuandoExistiremRegistros() {
        Clima clima = criarClimaCompleto();
        List<Clima> climas = List.of(clima);
        when(repository.findByNomeCidadeAndDataBetween(eq("Porto Alegre"), any(), any()))
                .thenReturn(climas);

        List<ClimaResponse> resultado = service.buscarPrevisaoProximosSeteDiasPorCidade("Porto Alegre", 7);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Porto Alegre", resultado.getFirst().nomeCidade());

        verify(repository).findByNomeCidadeAndDataBetween(eq("Porto Alegre"), any(), any());
    }

    @Test
    void deveAtualizarClimaQuandoRegistroExistir() {
        Clima clima = criarClimaCompleto();
        when(repository.findById(1L))
                .thenReturn(Optional.of(clima));
        when(repository.save(any(Clima.class)))
                .thenReturn(clima);

        var request = new AtualizarClimaRequest(
                "Porto Alegre",
                LocalDate.of(2026, 2, 20),
                Tempo.SOL,
                Tempo.NUBLADO,
                null,
                20,
                5.0,
                70,
                10.0
        );

        ClimaResponse resultado = service.atualizarClima(1L, request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Porto Alegre", resultado.nomeCidade());
        assertEquals(30, resultado.temperaturaMax());

        verify(repository).findById(1L);
        verify(repository).save(any(Clima.class));
    }

    @Test
    void deveDeletarClimaQuandoRegistroExistir() {
        Clima clima = criarClimaCompleto();
        when(repository.findById(1L))
                .thenReturn(Optional.of(clima));

        service.deletarClima(1L);

        verify(repository).findById(1L);
        verify(repository).delete(clima);
    }

    @Test
    void deveLancarExcecaoQuandoDeletarClimaInexistente() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ClimaNotFoundException.class,
                () -> service.deletarClima(1L)
        );
    }
}
