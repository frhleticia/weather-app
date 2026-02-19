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

    private Clima toEntity(CriarClimaRequest request) {
        return Clima.builder()
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
    }

    private ClimaResponse toResponse(Clima clima) {
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

    public ClimaResponse criarClima(CriarClimaRequest request) {
        Clima clima = toEntity(request);
        return toResponse(repository.save(clima));
    }

    public List<ClimaResponse> listarClimas() {
        List<Clima> climas = repository.findByDataGreaterThanEqual(LocalDate.now());

        if (climas.isEmpty()) {
            throw new RuntimeException("Nenhum clima encontrado");
        }

        return climas.stream().map(this::toResponse).toList();
    }

    public ClimaResponse buscarClimaPorId(Long id) {
        Clima clima = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clima não encontrado"));

        return toResponse(clima);
    }

    public List<ClimaResponse> buscarClimasPorCidade(String nomeCidade) {
        var hoje = LocalDate.now();
        List<Clima> climas = repository.findByNomeCidadeAndDataAfter(nomeCidade, hoje);

        return climas.stream().map(this::toResponse).toList();
    }

    public ClimaResponse buscarPrevisaoDoDiaPorCidade(String nomeCidade) {
        var hoje = LocalDate.now();
        Clima clima = repository.findByNomeCidadeAndData(nomeCidade, hoje)
                .orElseThrow(() -> new RuntimeException("Previsão do dia não encontrada"));

        return toResponse(clima);
    }

    public List<ClimaResponse> buscarPrevisaoProximosSeteDiasPorCidade(String nomeCidade, int dias) {
        var hoje = LocalDate.now();
        var dataFim = hoje.plusDays(dias);

        List<Clima> climas = repository.findByNomeCidadeAndDataBetween(nomeCidade, hoje, dataFim);

        return climas.stream().map(this::toResponse).toList();
    }

    public ClimaResponse editarClima(Long id, AtualizarClimaRequest request) {
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

        return toResponse(repository.save(clima));
    }

    public void deletarClima(Long id) {
        Clima clima = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clima não encontrado"));

        repository.delete(clima);
    }
}
