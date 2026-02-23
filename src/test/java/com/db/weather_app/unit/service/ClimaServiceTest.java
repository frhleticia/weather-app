package com.db.weather_app.unit.service;

import com.db.weather_app.dto.ClimaResponse;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.entity.Clima;
import com.db.weather_app.enums.Tempo;
import com.db.weather_app.mapper.ClimaMapper;
import com.db.weather_app.repository.ClimaRepository;
import com.db.weather_app.service.ClimaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClimaServiceTest {

    @InjectMocks
    private ClimaService service;

    @Mock
    private ClimaRepository repository;

    @Mock
    private ClimaMapper mapper;

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

        var entity = new Clima();
        var salvo = new Clima();
        salvo.setId(1L);

        var response = new ClimaResponse(
                1L,
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

         when(mapper.toEntity(request)).thenReturn(entity);
         when(repository.save(entity)).thenReturn(salvo);
         when(mapper.toResponse(salvo)).thenReturn(response);

         ClimaResponse resultado = service.criarClima(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Porto Alegre", resultado.nomeCidade());

        verify(repository).save(entity);
    }
}
