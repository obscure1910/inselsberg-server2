package de.obscure.web.db.entity

import arrow.optics.optics
import de.obscure.web.domain.CameraType

import javax.persistence.*

@Entity
@Table(name = "iberg_image")
@optics
data class ImageEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iberg_image_id_seq")
        @SequenceGenerator(name = "iberg_image_id_seq", sequenceName = "iberg_image_id_seq", allocationSize = 1)
        val id: Long?,
        @Enumerated(EnumType.ORDINAL)
        val camType: CameraType,
        val size: Long,
        val width: Int,
        val height: Int,
        val image: Array<Byte>,
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        val statistic: StatisticEntity
) {
    //needed by optics
    companion object
}
