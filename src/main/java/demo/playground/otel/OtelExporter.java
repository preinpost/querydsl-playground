package demo.playground.otel;

import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtelExporter {
    @Bean
    public LoggingSpanExporter loggingSpanExporter() {
        return LoggingSpanExporter.create();
    }

    @Bean
    public OtlpHttpSpanExporter otlpHttpSpanExporter() {
        return OtlpHttpSpanExporter.getDefault().toBuilder().build();
    }

    @Bean
    public LoggingMetricExporter loggingMetricExporter() {
        return LoggingMetricExporter.create();
    }

    @Bean
    public SystemOutLogRecordExporter batchLogRecordProcessor() {
        return SystemOutLogRecordExporter.create();
    }
}
