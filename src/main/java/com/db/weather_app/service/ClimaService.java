package com.db.weather_app.service;

import com.db.weather_app.mapper.ClimaMapper;
import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.ClimaResponse;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.entity.Clima;
import com.db.weather_app.exceptions.ClimaNotFoundException;
import com.db.weather_app.repository.ClimaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class ClimaService {

    private final ClimaRepository repository;

    public ClimaResponse criarClima(CriarClimaRequest request) {
        Clima clima = ClimaMapper.toEntity(request);
        Clima climaSalvo = repository.save(clima);

        return ClimaMapper.toResponse(climaSalvo);
    }

    public List<ClimaResponse> listarClimas() {
        List<Clima> climas = repository.findByDataGreaterThanEqual(LocalDate.now());

        if (climas.isEmpty()) {
            throw new ClimaNotFoundException("Nenhum clima encontrado");
        }

        return ClimaMapper.toResponseList(climas);
    }

    public ClimaResponse buscarClimaPorId(Long id) {
        Clima clima = repository.findById(id)
                .orElseThrow(ClimaNotFoundException::new);

        return ClimaMapper.toResponse(clima);
    }

    public List<ClimaResponse> buscarClimasPorCidade(String nomeCidade) {
        var hoje = LocalDate.now();
        List<Clima> climas = repository.findByNomeCidadeAndDataAfter(nomeCidade, hoje);

        return ClimaMapper.toResponseList(climas);
    }

    public ClimaResponse buscarPrevisaoDoDiaPorCidade(String nomeCidade) {
        var hoje = LocalDate.now();
        Clima clima = repository.findByNomeCidadeAndData(nomeCidade, hoje)
                .orElseThrow(() -> new ClimaNotFoundException("Previsão do dia não encontrada"));

        return ClimaMapper.toResponse(clima);
    }

    public List<ClimaResponse> buscarPrevisaoProximosSeteDiasPorCidade(String nomeCidade, int dias) {
        var hoje = LocalDate.now();
        var dataFim = hoje.plusDays(dias);

        List<Clima> climas = repository.findByNomeCidadeAndDataBetween(nomeCidade, hoje, dataFim);

        return ClimaMapper.toResponseList(climas);
    }

    public ClimaResponse atualizarClima(Long id, AtualizarClimaRequest request) {
        Clima clima = repository.findById(id)
                .orElseThrow(ClimaNotFoundException::new);

        ClimaMapper.updateEntity(clima, request);
        Clima atualizado = repository.save(clima);

        return ClimaMapper.toResponse(atualizado);
    }

    public void deletarClima(Long id) {
        Clima clima = repository.findById(id)
                .orElseThrow(ClimaNotFoundException::new);

        repository.delete(clima);
    }
}
