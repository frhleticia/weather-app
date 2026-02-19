package com.db.weather_app.dto;

import com.db.weather_app.enums.Tempo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record AtualizarClimaRequest(

        String nomeCidade,

        LocalDate data,

        Tempo tempoDia,

        Tempo tempoNoite,

        @Min(-100)
        @Max(100)
        Integer temperaturaMax,

        @Min(-100)
        @Max(100)
        Integer temperaturaMin,

        @PositiveOrZero
        Double precipitacao,

        @Min(0)
        @Max(100)
        Integer humidade,

        @PositiveOrZero
        Double velocidadeVento
) {
}
