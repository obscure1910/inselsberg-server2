package de.obscure.web.controller.validation

import javax.validation.Constraint

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class MaxDaysInThePast(
        val maxDaysInThePast: Long = 7,
        val message: String = "Given timestamp is too far in the past."
)