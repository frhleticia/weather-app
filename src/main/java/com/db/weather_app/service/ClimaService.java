package com.db.weather_app.service;

import com.db.weather_app.repository.ClimaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClimaService {

    private final ClimaRepository repository;
}
