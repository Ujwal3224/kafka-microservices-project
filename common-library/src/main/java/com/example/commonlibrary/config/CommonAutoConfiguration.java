package com.example.commonlibrary.config;

import com.example.commonlibrary.exception.GlobalExceptionHandler;
import com.example.commonlibrary.metrics.controller.MetricsController;
import com.example.commonlibrary.metrics.service.MetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example.commonlibrary")

public class CommonAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(CommonAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public MetricsService metricsService(
            MeterRegistry meterRegistry,
            @Value("${spring.application.name:default-service}") String serviceName) {
        log.info("Initializing MetricsService for: {}", serviceName);
        return new MetricsService(meterRegistry, serviceName);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler(MetricsService metricsService) {
        log.info("Initializing GlobalExceptionHandler");
        return new GlobalExceptionHandler(metricsService);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetricsController metricsController(MeterRegistry meterRegistry) {
        log.info("Initializing MetricsController");
        return new MetricsController(meterRegistry);
    }
}

