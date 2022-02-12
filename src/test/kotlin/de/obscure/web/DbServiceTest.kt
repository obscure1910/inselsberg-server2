package de.obscure.web

import arrow.core.nonEmptyListOf
import de.obscure.web.db.entity.StatisticEntity
import de.obscure.web.db.service.DbService
import de.obscure.web.domain.CameraType
import de.obscure.web.domain.Image
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import org.flywaydb.core.Flyway
import java.time.Instant

@MicronautTest
class DbServiceTest(
        private val dbService: DbService,
        private val flyway: Flyway) : StringSpec({


    val imageEntity = { s: StatisticEntity -> Image(null, CameraType.WEBCAM_IBERG, 0, 0, 0, emptyArray()).toImageEntity(s) }

    val statisticEntity = StatisticEntity(
            null,
            Instant.now(),
            20.0f,
            45.0f,
            1000.0f,
            "steigend",
            30.0f,
            40.0f,
            0.0f,
            1.2f,
            false
    )

    "findAllStatistics() should return all stored statistics and images" {
        val statisticEntity1 = statisticEntity.copy()
        dbService.saveStatistic(statisticEntity1, nonEmptyListOf(imageEntity(statisticEntity)))

        val loadedStatistics = dbService.findAllStatistics()
        loadedStatistics.shouldContain(statisticEntity1)

        val loadedImage = dbService.findImageByStatisticIdAndCamType(loadedStatistics.first().id!!, CameraType.WEBCAM_IBERG)
        loadedImage.shouldNotBeNull()
    }

    "findLastStatistic() should return statistic wih the highest id" {
        val currentTime = Instant.now()
        val statisticEntity1 = statisticEntity.copy(timestamp = currentTime)
        val statisticEntity2 = statisticEntity.copy(timestamp = currentTime.plusSeconds(1))

        dbService.saveStatistic(statisticEntity1, nonEmptyListOf(imageEntity(statisticEntity1)))
        dbService.saveStatistic(statisticEntity2, nonEmptyListOf(imageEntity(statisticEntity2)))

        val loadedStatistic = dbService.findLastStatistic()
        loadedStatistic?.id.shouldBe(2L)
        loadedStatistic?.timestamp.shouldBe(currentTime.plusSeconds(1))
    }

    "findByTimestampAfter() should return only entries after the given instant time" {
        val currentTime = Instant.now()
        val statisticEntity1 = statisticEntity.copy()
        val statisticEntity2 = statisticEntity.copy(timestamp = currentTime.plusSeconds(1))
        val statisticEntity3 = statisticEntity.copy(timestamp = currentTime.plusSeconds(2))

        //store statistics
        listOf(statisticEntity1, statisticEntity2, statisticEntity3).forEach {
            dbService.saveStatistic(it, nonEmptyListOf(imageEntity(it)))
        }

        dbService.findByTimestampAfter(currentTime)
                .shouldHaveSize(2)
                .shouldContainExactly(statisticEntity2, statisticEntity3)

        dbService.findByTimestampAfter(currentTime.plusSeconds(10))
                .shouldBeEmpty()
    }

}) {
    override fun beforeEach(testCase: TestCase) {
        flyway.clean()
        flyway.migrate()
    }
}