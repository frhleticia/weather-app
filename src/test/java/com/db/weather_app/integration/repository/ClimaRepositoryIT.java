package com.db.weather_app.integration.repository;

import com.db.weather_app.entity.Clima;
import com.db.weather_app.enums.Tempo;
import com.db.weather_app.repository.ClimaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ClimaRepositoryIT {

    @Autowired
    private ClimaRepository repository;

    @Test
    void deveRetornarClimaPorNomeCidadeEDataDepoisDeHoje() {
        var clima = new Clima(
                null,
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

        repository.save(clima);

        var pageable = PageRequest.of(0, 6);

        Page<Clima> page = repository.findByNomeCidadeAndDataGreaterThanEqual(
                "Porto Alegre", LocalDate.of(2026, 2, 19), pageable);

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void naoDeveRetornarClimaPorNomeCidadeEDataDepoisDeHoje() {
        var clima = new Clima(
                null,
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

        repository.save(clima);

        var pageable = PageRequest.of(0, 6);

        Page<Clima> page = repository.findByNomeCidadeAndDataGreaterThanEqual(
                "Porto Alegre", LocalDate.of(2026, 2, 21), pageable);

        assertTrue(page.isEmpty());
    }

    @Test
    void deveRetornarClimaPorNomeCidadeEDataUnicos() {
        var clima = new Clima(
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
        );

        repository.save(clima);

        var resultado = repository.findByNomeCidadeAndData(
                "Porto Alegre", LocalDate.now());

        assertTrue(resultado.isPresent());
        assertEquals(clima.getNomeCidade(), resultado.get().getNomeCidade());
    }

    @Test
    void naoDeveRetornarNadaQuandoNomeCidadeNaoEncontrado() {
        var resultado = repository.findByNomeCidadeAndData(
                "Cidade inexistente", LocalDate.now());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void naoDeveRetornarNadaQuandoDataNaoEncontrada() {
        var resultado = repository.findByNomeCidadeAndData(
                "Porto Alegre", LocalDate.now());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveRetornarClimasPorNomeCidadeEDataEntre() {
        var clima1 = new Clima(
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
        );

        var clima2 = new Clima(
                null,
                "Porto Alegre",
                LocalDate.now().plusDays(1),
                Tempo.SOL,
                Tempo.NUBLADO,
                20,
                20,
                2.0,
                20,
                20.0
        );

        var clima3 = new Clima(
                null,
                "Porto Alegre",
                LocalDate.now().plusDays(2),
                Tempo.SOL,
                Tempo.NUBLADO,
                10,
                10,
                1.0,
                10,
                10.0
        );

        repository.save(clima1);
        repository.save(clima2);
        repository.save(clima3);

        var resultado = repository.findByNomeCidadeAndDataBetween(
                "Porto Alegre", LocalDate.now(), LocalDate.now().plusDays(2));

        assertEquals(3, resultado.size());
    }

    @Test
    void deveRetornarClimasPorDataMaiorOuIgual() {
        var clima1 = new Clima(
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
        );

        var clima2 = new Clima(
                null,
                "Porto Alegre",
                LocalDate.now().plusDays(1),
                Tempo.SOL,
                Tempo.NUBLADO,
                20,
                20,
                2.0,
                20,
                20.0
        );

        var clima3 = new Clima(
                null,
                "Porto Alegre",
                LocalDate.now().minusDays(1),
                Tempo.SOL,
                Tempo.NUBLADO,
                10,
                10,
                1.0,
                10,
                10.0
        );

        repository.save(clima1);
        repository.save(clima2);
        repository.save(clima3);

        var pageable = PageRequest.of(0, 8);

        var resultado = repository.findByDataGreaterThanEqual(LocalDate.now(), pageable);

        assertEquals(2, resultado.getTotalElements());
    }
}
