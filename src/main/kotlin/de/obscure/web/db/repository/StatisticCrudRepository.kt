package de.obscure.web.db.repository

import de.obscure.web.db.entity.StatisticEntity
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.time.Instant

@Repository
interface StatisticCrudRepository : CrudRepository<StatisticEntity, Long> {

    @Query("FROM StatisticEntity AS stat WHERE stat.id=(SELECT max(id) FROM StatisticEntity)")
    fun findLast(): StatisticEntity?

    fun findByTimestampAfter(timestamp: Instant): Iterable<StatisticEntity>

}