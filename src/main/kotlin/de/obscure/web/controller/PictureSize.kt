package de.obscure.web.controller

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.QueryValue

@Introspected
data class PictureSize(
        @field:QueryValue val size: String) {

    fun toDomain(): de.obscure.web.domain.PictureSize {
        return when(size) {
            "preview" -> de.obscure.web.domain.PictureSize.PREVIEW
            "small" -> de.obscure.web.domain.PictureSize.SMALL
            else -> de.obscure.web.domain.PictureSize.NORMAL
        }
    }

}