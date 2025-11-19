package com.example.commonlibrary.metrics.service;


import com.example.commonlibrary.config.CommonAutoConfiguration;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MetricsService {
    private static final Logger log = LoggerFactory.getLogger(CommonAutoConfiguration.class);
    private final MeterRegistry meterRegistry;
    private final String serviceName;

    // Common counters
    private Counter totalApiCallsCounter;
    private Counter successfulApiCallsCounter;
    private Counter failedApiCallsCounter;

//    public MetricsService(MeterRegistry meterRegistry) {
//        this.meterRegistry = meterRegistry;
//        this.serviceName = "default-service";
//        initializeCommonMetrics();
//    }

    public MetricsService(MeterRegistry meterRegistry,
                          @Value("${spring.application.name:default-service}") String serviceName) {
        this.meterRegistry = meterRegistry;
        this.serviceName = serviceName;
        initializeCommonMetrics();
    }

    private void initializeCommonMetrics() {
        this.totalApiCallsCounter = Counter.builder("api.calls.total")
                .description("Total number of API calls")
                .tag("service", serviceName)
                .register(meterRegistry);

        this.successfulApiCallsCounter = Counter.builder("api.calls.success")
                .description("Number of successful API calls")
                .tag("service", serviceName)
                .register(meterRegistry);

        this.failedApiCallsCounter = Counter.builder("api.calls.failure")
                .description("Number of failed API calls")
                .tag("service", serviceName)
                .register(meterRegistry);
    }

    // Generic methods
    public void incrementTotalApiCalls() {
        totalApiCallsCounter.increment();
        log.debug("Total API calls incremented for {}", serviceName);
    }

    public void incrementSuccessfulApiCalls() {
        successfulApiCallsCounter.increment();
        log.debug("Successful API calls incremented for {}", serviceName);
    }

    public void incrementFailedApiCalls() {
        failedApiCallsCounter.increment();
        log.debug("Failed API calls incremented for {}", serviceName);
    }

    // Operation-specific methods
    public Counter createOperationCounter(String operation, String status) {
        return Counter.builder(serviceName + "." + operation + "." + status)
                .description(operation + " operation " + status)
                .tag("service", serviceName)
                .tag("operation", operation)
                .tag("status", status)
                .register(meterRegistry);
    }

    public Timer createOperationTimer(String operation) {
        return Timer.builder(serviceName + "." + operation + ".duration")
                .description("Time taken for " + operation + " operation")
                .tag("service", serviceName)
                .tag("operation", operation)
                .register(meterRegistry);
    }

    public void recordOperationSuccess(String operation) {
        Counter counter = createOperationCounter(operation, "success");
        counter.increment();
        incrementSuccessfulApiCalls();
        log.info("✅ {} operation successful - metrics recorded", operation);
    }

    public void recordOperationFailure(String operation) {
        Counter counter = createOperationCounter(operation, "failure");
        counter.increment();
        incrementFailedApiCalls();
        log.error("❌ {} operation failed - metrics recorded", operation);
    }

    public void recordOperationDuration(String operation, long startTime) {
        Timer timer = createOperationTimer(operation);
        long duration = System.currentTimeMillis() - startTime;
        log.debug("{} operation took {} ms", operation, duration);
    }

    // Gauge registration
    public void registerGauge(String name, String description, java.util.function.Supplier<Number> valueSupplier) {
        Gauge.builder(serviceName + "." + name, valueSupplier)  // ✅ Pass Supplier directly
                .description(description)
                .register(meterRegistry);

        log.info("Registered gauge: {}", name);
    }
}
