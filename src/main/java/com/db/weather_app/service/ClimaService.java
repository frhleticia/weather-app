package com.db.weather_app.service;

import com.db.weather_app.dto.RegistroClimaRequest;
import com.db.weather_app.entity.Clima;
import com.db.weather_app.repository.ClimaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class ClimaService {

    private final ClimaRepository repository;

    public Clima toEntity(RegistroClimaRequest request) {
        Clima clima = Clima.builder()
                .nomeCidade(request.nomeCidade())
                .data(request.data())
                .tempoDia(request.tempoDia())
                .tempoNoite(request.tempoNoite())
                .temperaturaMax(request.temperaturaMax())
                .temperaturaMin(request.temperaturaMin())
                .precipitacao(request.precipitacao())
                .humidade(request.humidade())
                .velocidadeVento(request.velocidadeVento())
                .build();

        return repository.save(clima);
    }

    public Clima buscarClimaPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clima não encontrado com id: " + id));
    }

    public Clima buscarClimaPorCidade(RegistroClimaRequest nomeCidade) {

    }
}
