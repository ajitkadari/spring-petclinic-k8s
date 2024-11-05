package org.springframework.samples.petclinic.api.config;

import io.micrometer.tracing.Tracer;
import io.micrometer.observation.Observation.Context;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.tracing.handler.TracingAwareMeterObservationHandler;
import io.micrometer.observation.ObservationTextPublisher;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ObservationConfiguration {

    private final Logger logger = LoggerFactory.getLogger(ObservationConfiguration.class);

    // Monitoring beans
    @Bean
    ObservationRegistry observationRegistry() {
      final var observationRegistry = ObservationRegistry.create();
      observationRegistry.observationConfig()
        .observationHandler(tracingAwareMeterObservationHandler())
        .observationHandler(observationTextPublisher());
      return observationRegistry;
    }

    @Bean
    public ObservationHandler<Context> tracingAwareMeterObservationHandler() {
        Tracer tracer = Tracer.NOOP;
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        return new TracingAwareMeterObservationHandler<>(
            new DefaultMeterObservationHandler(meterRegistry), tracer);
    } 

    @Bean
    public ObservationHandler<Context> observationTextPublisher() {
        return new ObservationTextPublisher(log::info);
    }

    // To have the @Observed support we need to register this aspect
    @Bean
    ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
      return new ObservedAspect(observationRegistry);
    }
}