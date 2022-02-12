package de.obscure.web.http

import arrow.core.NonEmptyList
import de.obscure.web.db.service.DbService
import de.obscure.web.domain.CameraType
import de.obscure.web.domain.Extensions.toImage
import de.obscure.web.domain.ImageProcessor
import de.obscure.web.domain.PictureSize
import io.micronaut.scheduling.annotation.Scheduled
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Singleton


//started by eagerInitSingletons in Application.kt
@Singleton
class WebPollingJob(
        private val dbService: DbService,
        private val statisticWebsite: StatisticWebsite,
        private val remotePicture: RemotePicture) {

    @Scheduled(fixedDelay = "300s", initialDelay = "10s")
    fun scheduledJob() {
        Flowable
                .defer { statisticWebsite.requestForData() }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .filter { statistic -> time.get().isBefore(statistic.timestamp) }
                .flatMap { statistic ->
                    Flowable
                            .zip(remotePicture.inselsberg().map { ImageProcessor.resizePicture(it, PictureSize.NORMAL).toImage(CameraType.WEBCAM_IBERG) },
                                    remotePicture.panomax().map { ImageProcessor.resizePicture(it, PictureSize.NORMAL).toImage(CameraType.PANOMAX_IBERG) },
                                    remotePicture.tabarz().map { ImageProcessor.resizePicture(it, PictureSize.NORMAL).toImage(CameraType.WEBCAM_TABARZ) }) { inselsberg, panomax, tabarz ->
                                val statisticEntity = statistic.toStatisticEntity()
                                val allImageEntities = listOf(inselsberg, panomax, tabarz).map { it.toImageEntity(statisticEntity) }
                                Flowable
                                        .defer { Flowable.just(dbService.saveStatistic(statisticEntity, NonEmptyList.fromListUnsafe(allImageEntities))) }
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(
                                                { time.set(statistic.timestamp) },
                                                { err ->
                                                    val message = err.message
                                                    if (message != null && message.contains("duplicate key")) {
                                                        time.set(statistic.timestamp)
                                                    }
                                                })
                            }
                }
                .ignoreElements()
                .blockingGet()
    }

    companion object {
        val time: AtomicReference<Instant> = AtomicReference(Instant.EPOCH)
        val LOG = LoggerFactory.getLogger(WebPollingJob::class.java)
    }

}

