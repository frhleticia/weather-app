package com.db.weather_app.dto;

import com.db.weather_app.enums.Tempo;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CriarClimaRequest(

        @NotBlank
        String nomeCidade,

        @NotNull
        LocalDate data,

        @NotNull
        Tempo tempoDia,

        @NotNull
        Tempo tempoNoite,

        @NotNull
        @Min(-100)
        @Max(100)
        Integer temperaturaMax,

        @NotNull
        @Min(-100)
        @Max(100)
        Integer temperaturaMin,

        @NotNull
        @PositiveOrZero
        Double precipitacao,

        @NotNull
        @Min(0)
        @Max(100)
        Integer humidade,

        @NotNull
        @PositiveOrZero
        Double velocidadeVento
) {
}
