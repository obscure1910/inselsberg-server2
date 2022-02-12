package de.obscure.web

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
            .args(*args)
            .eagerInitSingletons(true)
            .packages("de.obscure.web")
            .start()
}

