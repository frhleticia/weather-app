package com.db.weather_app.controller;

import com.db.weather_app.service.ClimaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/climas")
@RequiredArgsConstructor
public class ClimaController {

    private final ClimaService service;
}
