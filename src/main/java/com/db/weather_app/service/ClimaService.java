package com.db.weather_app.service;

import com.db.weather_app.dto.RegistroClimaRequest;
import com.db.weather_app.entity.Clima;
import com.db.weather_app.repository.ClimaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class ClimaService {

    private final ClimaRepository repository;

    public Clima toEntity(RegistroClimaRequest request) {
        return repository.save(Clima.builder()
                .nomeCidade(request.nomeCidade())
                .data(request.data())
                .tempoDia(request.tempoDia())
                .tempoNoite(request.tempoNoite())
                .temperaturaMax(request.temperaturaMax())
                .temperaturaMin(request.temperaturaMin())
                .precipitacao(request.precipitacao())
                .humidade(request.humidade())
                .velocidadeVento(request.velocidadeVento())
                .build());
    }

    public List<Clima> listarClimas() {
        List<Clima> climas = repository.findByDataGreaterThanEqual(LocalDate.now());

        if (climas.isEmpty()) {
            throw new RuntimeException("Nenhum clima encontrado");
        }

        return climas;
    }

    public Clima buscarClimaPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clima não encontrado"));
    }

    public List<Clima> buscarClimasPorCidade(String nomeCidade) {
        var hoje = LocalDate.now();
        return repository.findByCidadeAndDataAfter(nomeCidade, hoje);
    }

    public Clima buscarPrevisaoDoDiaPorCidade(String nomeCidade) {
        var hoje = LocalDate.now();

        return repository.findByCidadeAndData(nomeCidade, hoje)
                .orElseThrow(() -> new RuntimeException("Previsão do dia não encontrada"));
    }

    public List<Clima> buscarPrevisaoDosProximosSeteDiasPorCidade(String nomeCidade, int dias) {
        var hoje = LocalDate.now();
        var dataFim = hoje.plusDays(dias);

        return repository.findByCidadeAndDataBetween(nomeCidade, hoje, dataFim);
    }
}
