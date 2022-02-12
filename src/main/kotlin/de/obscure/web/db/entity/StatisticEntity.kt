package de.obscure.web.db.entity

import arrow.optics.optics
import de.obscure.web.domain.Statistic
import de.obscure.web.domain.iso
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "iberg_statistic")
@optics
data class StatisticEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iberg_statistic_id_seq")
        @SequenceGenerator(name = "iberg_statistic_id_seq", sequenceName = "iberg_statistic_id_seq", allocationSize = 1)
        val id: Long?,
        val timestamp: Instant,
        val temperature: Float,
        val humidity: Float,
        val airPressure: Float,
        val airPressureTrend: String,
        val windVelocity: Float,
        val windGust: Float,
        val rain: Float,
        val rainLastHours: Float,
        val isLiftOpen: Boolean
) {

    // workaround micronaut does not generate getter for boolean variables - or micronaut jpa did not consider java bean convention for boolean variables
    fun getIsLiftOpen(): Boolean = isLiftOpen

    fun toStatistic(): Statistic {
        val tuple = StatisticEntity.iso.get(this)
        return Statistic.iso.set(tuple)
    }

    //needed by optics
    companion object
}