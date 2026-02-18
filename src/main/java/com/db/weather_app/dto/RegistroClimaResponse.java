package com.db.weather_app.dto;

import com.db.weather_app.enums.Tempo;

import java.time.LocalDate;

public record RegistroClimaResponse(

        Long id,

        String nomeCidade,

        LocalDate data,

        Tempo tempoDia,

        Tempo tempoNoite,

        Integer temperaturaMax,

        Integer temperaturaMin,

        Double precipitacao,

        Integer humidade,

        Double velocidadeVento) {
}
