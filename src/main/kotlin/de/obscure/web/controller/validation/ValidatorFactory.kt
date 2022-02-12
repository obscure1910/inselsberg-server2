package de.obscure.web.controller.validation

import io.micronaut.context.annotation.Factory
import io.micronaut.validation.validator.constraints.ConstraintValidator
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Singleton

@Factory
class ValidatorFactory {

    @Singleton
    fun maxDaysInThePastValidator(): ConstraintValidator<MaxDaysInThePast, Instant> {
        return ConstraintValidator { value, annotation, _ ->
            // afterEquals
            val referenceValue = annotation.longValue("maxDaysInThePast").asLong
            value == null || value.isAfter(Instant.now().minus(referenceValue, ChronoUnit.DAYS).minusSeconds(1))
        }
    }

}