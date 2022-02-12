package de.obscure.web.controller.converter

import io.micronaut.core.convert.ConversionContext
import io.micronaut.core.convert.TypeConverter
import java.time.Instant
import java.util.*
import javax.inject.Singleton

/**
 * Convert an String to Instant. It is used in the controller of this service
 */
@Singleton
class StringToInstant : TypeConverter<String, Instant> {

    override fun convert(`object`: String?, targetType: Class<Instant>?, context: ConversionContext?): Optional<Instant> {
        return Optional.ofNullable(`object`)
                .flatMap { string ->
                    return@flatMap try {
                        Optional.of(string.toLong())
                    } catch (ex: Exception) {
                        Optional.empty<Long>()
                    }
                }
                .map { Instant.ofEpochSecond(it) }
    }

}