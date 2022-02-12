package de.obscure.web.controller.validation

import de.obscure.web.controller.WeatherController
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.validation.ConstraintViolationException

@MicronautTest
class ValidatorTest(controller: WeatherController) : StringSpec({

    "ReadStatisticSince should raise an error if the given timestamp exceeds the limit " {
        val count = 7L
        shouldThrow<ConstraintViolationException> {
            controller.readStatisticSince(Instant.now().minus(count, ChronoUnit.DAYS).minusSeconds(10))
        }
        shouldNotThrow<ConstraintViolationException> {
            controller.readStatisticSince(Instant.now().minus(count , ChronoUnit.DAYS))
        }
    }

})