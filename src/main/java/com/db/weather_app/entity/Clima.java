package com.db.weather_app.entity;

import com.db.weather_app.enums.Tempo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"nome_cidade", "data"}
        ))
@Entity
public class Clima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_cidade", nullable = false)
    private String nomeCidade;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(name = "tempo_dia", nullable = false)
    private Tempo tempoDia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tempo_noite", nullable = false)
    private Tempo tempoNoite;

    @Column(name = "temperatura_max", nullable = false)
    private int temperaturaMax;

    @Column(name = "temperatura_min", nullable = false)
    private int temperaturaMin;

    @Column(name = "precipitacao", nullable = false)
    private double precipitacao;

    @Column(name = "humidade", nullable = false)
    private int humidade;

    @Column(name = "velocidade_vento", nullable = false)
    private double velocidadeVento;

}
