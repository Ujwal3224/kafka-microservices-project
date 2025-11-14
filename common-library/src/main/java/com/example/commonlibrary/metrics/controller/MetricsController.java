package com.example.commonlibrary.metrics.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/metrics")

public class MetricsController {

    private final MeterRegistry meterRegistry;

    public MetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/summary")
    public Map<String, Object> getMetricsSummary() {
        Map<String, Object> metrics = new HashMap<>();

        // Total API calls
        Counter totalCalls = meterRegistry.find("api.calls.total").counter();
        metrics.put("totalApiCalls", totalCalls != null ? totalCalls.count() : 0);

        // Successful calls
        Counter successCalls = meterRegistry.find("api.calls.success").counter();
        metrics.put("successfulApiCalls", successCalls != null ? successCalls.count() : 0);

        // Failed calls
        Counter failedCalls = meterRegistry.find("api.calls.failure").counter();
        metrics.put("failedApiCalls", failedCalls != null ? failedCalls.count() : 0);

        // Success rate
        double successRate = 0;
        if (totalCalls != null && totalCalls.count() > 0) {
            successRate = (successCalls != null ? successCalls.count() : 0) / totalCalls.count() * 100;
        }
        metrics.put("successRate", String.format("%.2f%%", successRate));

        return metrics;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("message", "Metrics service is running");
        return health;
    }
}
