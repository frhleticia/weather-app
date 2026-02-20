package com.db.weather_app.controller;

import com.db.weather_app.dto.AtualizarClimaRequest;
import com.db.weather_app.dto.ClimaResponse;
import com.db.weather_app.dto.CriarClimaRequest;
import com.db.weather_app.service.ClimaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dados-meteorologicos")
@RequiredArgsConstructor
public class ClimaController {

    private final ClimaService service;

    @PostMapping("/registrar-cidade")
    public ResponseEntity<ClimaResponse> criarClima(
            @RequestBody @Valid CriarClimaRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criarClima(request));
    }

    @PatchMapping("/{id}/atualizar-registro")
    public ResponseEntity<ClimaResponse> atualizarClima(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarClimaRequest request) {

        return ResponseEntity.ok(service.atualizarClima(id, request));
    }

    @GetMapping
    public ResponseEntity<List<ClimaResponse>> listarClimas() {

        return ResponseEntity.ok(service.listarClimas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClimaResponse> buscarClimaPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.buscarClimaPorId(id));
    }

    @GetMapping("/cidade/{nomeCidade}")
    public ResponseEntity<List<ClimaResponse>> listarClimasPorCidade(
            @RequestParam String nomeCidade) {

        return ResponseEntity.ok(service.buscarClimasPorCidade(nomeCidade));
    }

    @GetMapping("/cidade/{nomeCidade}/previsao-dia")
    public ResponseEntity<ClimaResponse> buscarPrevisaoDoDiaPorCidade(
            @PathVariable String nomeCidade) {

        return ResponseEntity.ok(service.buscarPrevisaoDoDiaPorCidade(nomeCidade));
    }

    @GetMapping("/cidade/{nomeCidade}/previsao-semana")
    public ResponseEntity<List<ClimaResponse>> buscarPrevisaoProximosSeteDiasPorCidade(
            @RequestParam String nomeCidade,
            @RequestParam int dia
    ) {
        return ResponseEntity.ok(service.buscarPrevisaoProximosSeteDiasPorCidade(nomeCidade, dia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarClima(@PathVariable Long id) {
        service.deletarClima(id);

        return ResponseEntity.noContent().build();
    }
}
