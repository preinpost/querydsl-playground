package demo.playground.aspect;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
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
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("run aspect");
        log.info("joinPoint.getSignature().getName() = {}", joinPoint.getSignature().getName());

        Tracer tracer = openTelemetry.getTracer(joinPoint.getSignature().getName());

        // 스팬 시작
        Span span = tracer.spanBuilder(joinPoint.getSignature().toShortString()).startSpan();

        try {
            // 메서드 실행
            return joinPoint.proceed();
        } catch (Throwable t) {
            // 오류 기록
            span.recordException(t);
            throw t;
        } finally {
            // 스팬 종료
            span.end();
        }
    }

}
