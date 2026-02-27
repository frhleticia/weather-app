package com.db.weather_app.repository;

import com.db.weather_app.entity.Clima;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClimaRepository extends JpaRepository<Clima, Long> {

    Page<Clima> findByNomeCidadeAndDataGreaterThanEqual(
            String nomeCidade,
            LocalDate data,
            Pageable pageable
    );

    Optional<Clima> findByNomeCidadeAndData(
            String nomeCidade,
            LocalDate data
    );

    List<Clima> findByNomeCidadeAndDataBetween(
            String nomeCidade,
            LocalDate dataInicio,
            LocalDate dataFim
    );

    Page<Clima> findByDataGreaterThanEqual(LocalDate data, Pageable pageable);
}
