package com.db.weather_app.service;

import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.ClimaResponse;
import com.db.weather_app.dto.CriarClimaRequest;
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

    public Clima toEntity(CriarClimaRequest request) {
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

    public ClimaResponse toResponse(Clima clima) {
        return new ClimaResponse(
                clima.getId(),
                clima.getNomeCidade(),
                clima.getData(),
                clima.getTempoDia(),
                clima.getTempoNoite(),
                clima.getTemperaturaMax(),
                clima.getTemperaturaMin(),
                clima.getPrecipitacao(),
                clima.getHumidade(),
                clima.getVelocidadeVento()
        );
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

    public List<Clima> buscarPrevisaoProximosSeteDiasPorCidade(String nomeCidade, int dias) {
        var hoje = LocalDate.now();
        var dataFim = hoje.plusDays(dias);

        return repository.findByCidadeAndDataBetween(nomeCidade, hoje, dataFim);
    }

    public void editarClima(Long id, AtualizarClimaRequest request) {
        Clima clima = repository.findById(id).orElseThrow(() -> new RuntimeException("Clima não encontrado"));

        if (request.nomeCidade() != null) {
            clima.setNomeCidade(request.nomeCidade());
        }

        if (request.data() != null) {
            clima.setData(request.data());
        }

        if (request.tempoDia() != null) {
            clima.setTempoDia(request.tempoDia());
        }

        if (request.tempoNoite() != null) {
            clima.setTempoNoite(request.tempoNoite());
        }

        if (request.temperaturaMax() != null) {
            clima.setTemperaturaMax(request.temperaturaMax());
        }

        if (request.temperaturaMin() != null) {
            clima.setTemperaturaMin(request.temperaturaMin());
        }

        if (request.precipitacao() != null) {
            clima.setPrecipitacao(request.precipitacao());
        }

        if (request.humidade() != null) {
            clima.setHumidade(request.humidade());
        }

        if (request.velocidadeVento() != null) {
            clima.setVelocidadeVento(request.velocidadeVento());
        }

        repository.save(clima);
    }

    public void deletarClima(Long id) {
        Clima clima = buscarClimaPorId(id);
        repository.delete(clima);
    }
}
