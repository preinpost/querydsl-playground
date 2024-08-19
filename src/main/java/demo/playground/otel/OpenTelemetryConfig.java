package demo.playground.otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenTelemetryConfig {

    private final LoggingSpanExporter loggingSpanExporter;
    private final OtlpHttpSpanExporter otlpHttpSpanExporter;
    private final LoggingMetricExporter loggingMetricExporter;
    private final SystemOutLogRecordExporter systemOutLogRecordExporter;

    @Bean
    public OpenTelemetry openTelemetry() {
        Resource resource =
            Resource.getDefault().toBuilder().put(ResourceAttributes.SERVICE_NAME, "demo-server")
                .put(ResourceAttributes.SERVICE_VERSION, "0.1.0").build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.create(otlpHttpSpanExporter))
            .setResource(resource).build();

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder().registerMetricReader(
                PeriodicMetricReader.builder(loggingMetricExporter).build())
            .setResource(resource).build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder().addLogRecordProcessor(
                BatchLogRecordProcessor.builder(systemOutLogRecordExporter).build())
            .setResource(resource).build();

        OpenTelemetry openTelemetry =
            OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
                .setMeterProvider(sdkMeterProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .setPropagators(ContextPropagators.create(
                    TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(),
                        W3CBaggagePropagator.getInstance()))).buildAndRegisterGlobal();

        return openTelemetry;
    }
}
