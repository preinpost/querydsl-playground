package demo.playground.aspect;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TracingAspect {

    private final OpenTelemetry openTelemetry;

    @Pointcut("execution(* demo..*(..))")
    private void allMethods() {}

    @Pointcut("execution(* demo.playground..*(..))")
    private void playgroundMethod() {}

    @Pointcut("execution(* demo.playground.jpa..*(..))")
    private void playgroundJpaMethod() {}

    @Pointcut("execution(* demo.playground.repository..*(..))")
    private void playgroundRepositoryMethod() {}

    @Pointcut("execution(* demo.playground.otel..*(..))")
    private void otelPackage() {}

    @Pointcut("execution(* demo.playground.jpa..*(..))")
    private void jpaMethod() {}

    @Pointcut("execution(* demo.playground.controller..*(..))")
    private void playgroundControllerMethod() {}


    @Around("allMethods() && !otelPackage()")
    public Object controllerAop(ProceedingJoinPoint joinPoint) throws Throwable {

        String name = joinPoint.getSignature().getName();
        log.info("name = {}", name);

        Tracer tracer = openTelemetry.getTracer(joinPoint.getSignature().getName());
        SpanBuilder spanBuilder = tracer.spanBuilder(joinPoint.getSignature().toShortString());

        Span span = spanBuilder.startSpan();

        Tracer tracer1 = GlobalOpenTelemetry.getTracer(joinPoint.getSignature().getName());
        log.info("tracer1 = {}", tracer1);

        SpanContext spanContext = span.getSpanContext();
        String spanId = spanContext.getSpanId();
        log.info("spanId = {}", spanId);

        Scope scope1 = span.makeCurrent();
        Scope scope2 = Baggage.current().makeCurrent();
//        scope2.close();
        //        log.debug("scope = {}", scope);

        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            // 오류 기록
            span.recordException(t);
            throw t;
        } finally {
            // 스팬 종료
            scope1.close();
            span.end();
        }
    }
}
