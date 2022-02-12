package de.obscure.web.db.service

import arrow.core.NonEmptyList

import de.obscure.web.db.entity.ImageEntity
import de.obscure.web.db.entity.StatisticEntity
import de.obscure.web.db.entity.statistic
import de.obscure.web.db.repository.ImageCrudRepository
import de.obscure.web.db.repository.StatisticCrudRepository
import de.obscure.web.domain.CameraType
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.constructor.DuplicateKeyException
import java.time.Instant
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class DbServiceImpl(
        val statisticCrudRepository: StatisticCrudRepository,
        val imageCrudRepository: ImageCrudRepository
) : DbService {

    @Transactional
    override fun saveStatistic(statisticEntity: StatisticEntity, images: NonEmptyList<ImageEntity>) {
        val newStatistic = statisticCrudRepository.save(statisticEntity)
        imageCrudRepository.saveAll(images.map { ImageEntity.statistic.set(it, newStatistic) })
    }

    override fun findAllStatistics(): List<StatisticEntity> {
        return statisticCrudRepository.findAll().toList()
    }

    override fun findImageByStatisticIdAndCamType(statisticId: Long, camType: CameraType): ImageEntity? {
        return imageCrudRepository.findByStatisticIdAndCamType(statisticId, camType)
    }

    override fun findLastStatistic(): StatisticEntity? {
        return statisticCrudRepository.findLast()
    }

    override fun findByTimestampAfter(timestamp: Instant): List<StatisticEntity> {
        return statisticCrudRepository.findByTimestampAfter(timestamp).toList()
    }

    companion object {
        val LOG = LoggerFactory.getLogger(DbServiceImpl::class.java)
    }

}