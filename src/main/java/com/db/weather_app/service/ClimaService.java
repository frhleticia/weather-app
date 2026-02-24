package com.db.weather_app.service;

import com.db.weather_app.mapper.ClimaMapper;
import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.ClimaResponse;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.entity.Clima;
import com.db.weather_app.exceptions.ClimaNotFoundException;
import com.db.weather_app.repository.ClimaRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class ClimaService {

    private final ClimaRepository repository;

    private final ClimaMapper mapper;

    public ClimaResponse criarClima(CriarClimaRequest request) {
        boolean jaExiste = repository.findByNomeCidadeAndData(request.nomeCidade(), request.data()).isPresent();

        if (jaExiste) {
            throw new DataIntegrityViolationException("Já existe um clima cadastrado para essa cidade nessa data");
        }

        Clima clima = mapper.toEntity(request);
        Clima climaSalvo = repository.save(clima);

        return mapper.toResponse(climaSalvo);
    }

    public Page<ClimaResponse> listarClimas(Pageable pageable) {
        Page<Clima> climasPage = repository.findByDataGreaterThanEqual(LocalDate.now(), pageable);

        if (climasPage.isEmpty()) {
            throw new ClimaNotFoundException("Nenhum clima encontrado");
        }

        return climasPage.map(mapper::toResponse);
    }

    public ClimaResponse buscarClimaPorId(Long id) {
        Clima clima = repository.findById(id)
                .orElseThrow(ClimaNotFoundException::new);

        return mapper.toResponse(clima);
    }

    public Page<ClimaResponse> buscarClimasPorCidade(String nomeCidade, Pageable pageable) {
        var hoje = LocalDate.now();
        Page<Clima> climasPage = repository.findByNomeCidadeAndDataGreaterThanEqual(nomeCidade, hoje, pageable);

        return climasPage.map(mapper::toResponse);
    }

    public ClimaResponse buscarPrevisaoDoDiaPorCidade(String nomeCidade) {
        var hoje = LocalDate.now();
        Clima clima = repository.findByNomeCidadeAndData(nomeCidade, hoje)
                .orElseThrow(() -> new ClimaNotFoundException("Previsão do dia não encontrada"));

        return mapper.toResponse(clima);
    }

    public List<ClimaResponse> buscarPrevisaoProximosSeteDiasPorCidade(String nomeCidade, int dias) {
        var hoje = LocalDate.now();
        var dataFim = hoje.plusDays(dias);

        List<Clima> climas = repository.findByNomeCidadeAndDataBetween(nomeCidade, hoje, dataFim);

        return mapper.toResponseList(climas);
    }

    public ClimaResponse atualizarClima(Long id, AtualizarClimaRequest request) {
        Clima clima = repository.findById(id)
                .orElseThrow(ClimaNotFoundException::new);

        mapper.updateEntity(clima, request);
        Clima atualizado = repository.save(clima);

        return mapper.toResponse(atualizado);
    }

    public void deletarClima(Long id) {
        Clima clima = repository.findById(id)
                .orElseThrow(ClimaNotFoundException::new);

        repository.delete(clima);
    }
}
