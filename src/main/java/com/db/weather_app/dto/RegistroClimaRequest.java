package com.db.weather_app.dto;

import com.db.weather_app.enums.Tempo;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegistroClimaRequest(

        @NotBlank
        String nomeCidade,

        @NotNull
        LocalDate data,

        @NotNull
        Tempo tempoDia,

        @NotNull
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
