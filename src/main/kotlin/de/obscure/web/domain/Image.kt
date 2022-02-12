package de.obscure.web.domain

import arrow.core.plus
import arrow.optics.optics
import de.obscure.web.db.entity.ImageEntity
import de.obscure.web.db.entity.StatisticEntity
import de.obscure.web.db.entity.iso
import io.micronaut.core.annotation.Introspected

@Introspected
@optics
data class Image(
        val id: Long?,
        val camType: CameraType,
        val size: Long,
        val width: Int,
        val height: Int,
        val image: Array<Byte>) {

    fun toImageEntity(statistcEntity: StatisticEntity): ImageEntity {
        val tupledImage = Image.iso.get(this).plus(statistcEntity)
        return ImageEntity.iso.reverseGet(tupledImage)
    }

    //needed by optics
    companion object
}

