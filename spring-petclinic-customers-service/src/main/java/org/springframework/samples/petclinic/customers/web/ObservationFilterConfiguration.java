package org.springframework.samples.petclinic.customers.web;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ServerHttpObservationFilter;

@Configuration
public class ObservationFilterConfiguration {

    // if an ObservationRegistry is already configured
    @ConditionalOnBean(ObservationRegistry.class)
    // if we do not use Actuator
    @ConditionalOnMissingBean(ServerHttpObservationFilter.class)
    @Bean
    public ServerHttpObservationFilter observationFilter(ObservationRegistry registry) {
        return new ServerHttpObservationFilter(registry);
    }

}