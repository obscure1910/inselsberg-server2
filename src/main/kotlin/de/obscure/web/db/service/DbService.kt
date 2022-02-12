package de.obscure.web.db.service

import arrow.core.NonEmptyList
import de.obscure.web.db.entity.ImageEntity
import de.obscure.web.db.entity.StatisticEntity
import de.obscure.web.domain.CameraType
import java.time.Instant
import javax.transaction.Transactional

interface DbService {

    fun saveStatistic(statisticEntity: StatisticEntity, images: NonEmptyList<ImageEntity>)
    fun findAllStatistics(): List<StatisticEntity>
    fun findImageByStatisticIdAndCamType(statisticId: Long, camType: CameraType): ImageEntity?
    fun findLastStatistic(): StatisticEntity?
    fun findByTimestampAfter(timestamp: Instant): List<StatisticEntity>
}