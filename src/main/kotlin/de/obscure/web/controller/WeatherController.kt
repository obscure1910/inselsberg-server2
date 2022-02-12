package de.obscure.web.controller

import de.obscure.web.controller.validation.MaxDaysInThePast
import de.obscure.web.db.service.DbService
import de.obscure.web.domain.CameraType
import de.obscure.web.domain.Extensions.toByteArray
import de.obscure.web.domain.ImageProcessor
import de.obscure.web.domain.Statistic
import de.obscure.web.domain.StatisticInformation
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import org.slf4j.LoggerFactory
import java.time.Instant

@Controller
open class WeatherController(val dbService: DbService) {

    private val LOG = LoggerFactory.getLogger(WeatherController::class.java)


    @Get("/statistics")
    @ExecuteOn(TaskExecutors.IO)
    open fun readStatisticSince(@MaxDaysInThePast(7) @QueryValue since: Instant): StatisticInformation {
        val statistics = dbService.findByTimestampAfter(since).map { it.toStatistic() }.toList()
        return StatisticInformation(statistics)
    }

    @Get("/statistics/images/{statisticId}", produces = [MediaType.IMAGE_JPEG])
    @ExecuteOn(TaskExecutors.IO)
    open fun readImageByIdAndCameraType(@PathVariable statisticId: Long,
                                        @QueryValue cameraType: CameraType,
                                        @RequestBean pictureSize: PictureSize): ByteArray? {
        return dbService.findImageByStatisticIdAndCamType(statisticId, cameraType)?.image?.let {
            val bytes = it.toByteArray()
            if (pictureSize.toDomain() !is de.obscure.web.domain.PictureSize.NORMAL) {
                ImageProcessor
                        .resizePicture(bytes, pictureSize.toDomain())
                        .toByteArray()
            } else {
                bytes
            }
        }
    }

}