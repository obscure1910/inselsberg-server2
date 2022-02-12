package de.obscure.web.db.repository

import de.obscure.web.db.entity.ImageEntity
import de.obscure.web.domain.CameraType
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import javax.persistence.EntityManager

@Repository
interface ImageCrudRepository : CrudRepository<ImageEntity, Long> {

    @Query("FROM ImageEntity AS img WHERE img.camType= :camType AND img.statistic.id= :statisticId")
    fun findByStatisticIdAndCamType(statisticId: Long, camType: CameraType): ImageEntity?

}