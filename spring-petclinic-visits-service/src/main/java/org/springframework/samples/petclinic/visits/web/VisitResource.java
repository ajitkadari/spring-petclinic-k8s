/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.visits.web;

import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Timed("petclinic.visit")
class VisitResource {

    private final VisitRepository visitRepository;

    @Autowired
    private final ObservationRegistry observationRegistry;

    @Observed(name = "visit:saveVisitByPetId")
    @PostMapping("owners/*/pets/{petId}/visits")
    @ResponseStatus(HttpStatus.CREATED)
    Visit create(
        @Valid @RequestBody Visit visit,
        @PathVariable("petId") int petId) {

        visit.setPetId(petId);
        log.info("Saving visit {}", visit);
        return visitRepository.save(visit);
    }

    @Observed(name = "visit:getVisitsByPetId")
    @GetMapping("owners/*/pets/{petId}/visits")
    List<Visit> visits(@PathVariable("petId") int petId) {
        log.info("Getting visits by petId {}", String.valueOf(petId));
        return visitRepository.findByPetId(petId);
    }

    @Observed(name = "visit:getVisitsByPetIds")
    @GetMapping("pets/visits")
    Visits visitsMultiGet(@RequestParam("petId") List<Integer> petIds) {
        final List<Visit> byPetIdIn = visitRepository.findByPetIdIn(petIds);
        log.info("Getting visits by petIds");
        return new Visits(byPetIdIn);
    }

    @Value
    static class Visits {
        private final List<Visit> items;
    }
}
