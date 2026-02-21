package com.db.weather_app.mapper;

import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.ClimaResponse;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.entity.Clima;

import java.util.List;

public class ClimaMapper {

    public static Clima toEntity(CriarClimaRequest request) {
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

    public static ClimaResponse toResponse(Clima clima) {
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

    public static void updateEntity(Clima clima, AtualizarClimaRequest request) {

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
    }

    public static List<ClimaResponse> toResponseList(List<Clima> climas) {
        return climas.stream()
                .map(ClimaMapper::toResponse)
                .toList();
    }
}
