package org.springframework.samples.petclinic.customers.config;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.ObservationTextPublisher;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.TracingAwareMeterObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ObservationConfiguration {

    // Monitoring beans
    @Bean
    public ObservationRegistry observationRegistry() {
      final var observationRegistry = ObservationRegistry.create();
      observationRegistry.observationConfig()
        .observationHandler(tracingAwareMeterObservationHandler())
        .observationHandler(observationTextPublisher());
      return observationRegistry;
    }

    @Bean
    public ObservationHandler<Observation.Context> tracingAwareMeterObservationHandler() {
        Tracer tracer = Tracer.NOOP;
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        return new TracingAwareMeterObservationHandler<>(
            new DefaultMeterObservationHandler(meterRegistry), tracer);
    }

    @Bean
    public ObservationHandler<Observation.Context> observationTextPublisher() {
        return new ObservationTextPublisher(log::info);
    }

    // To have the @Observed support we need to register this aspect
    @Bean
    ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
      return new ObservedAspect(observationRegistry);
    }

    // To have the @Counted support we need to register this aspect
    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
