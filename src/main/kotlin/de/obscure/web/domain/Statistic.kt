package de.obscure.web.domain

import arrow.optics.optics
import com.fasterxml.jackson.annotation.JsonGetter
import de.obscure.web.db.entity.StatisticEntity
import de.obscure.web.db.entity.iso
import java.time.Instant

@optics
data class Statistic(
        var id: Long?,
        var timestamp: Instant,
        var temperature: Float,
        var humidity: Float,
        var airPressure: Float,
        var airPressureTrend: String,
        var windVelocity: Float,
        var windGust: Float,
        var rain: Float,
        var rainLastHours: Float,
        var isLiftOpen: Boolean
) {
    fun toStatisticEntity(): StatisticEntity {
        val tupled = Statistic.iso.get(this)
        return StatisticEntity.iso.set(tupled)
    }

    @JsonGetter("timestamp")
    fun getTimestampAsLong(): Long? {
        return timestamp.epochSecond
    }

    //needed by optics
    companion object
}