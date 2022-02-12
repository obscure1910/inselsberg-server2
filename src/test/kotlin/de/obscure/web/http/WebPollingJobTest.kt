package de.obscure.web.http

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import de.obscure.web.db.service.DbService
import de.obscure.web.domain.CameraType
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import org.awaitility.Awaitility.with
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Assertions.assertAll
import java.util.concurrent.TimeUnit


@MicronautTest
class WebPollingJobTest(
        private val webPollingJob: WebPollingJob,
        private val dbService: DbService,
        private val flyway: Flyway) : StringSpec({

    "via polling the statistic and images should be stored" {
        webPollingJob.scheduledJob()
        with().atMost(10, TimeUnit.MINUTES)
                .pollInterval(5, TimeUnit.SECONDS)
                .pollDelay(30, TimeUnit.SECONDS)
                .untilAsserted {
                    assertAll(
                            { dbService.findAllStatistics().shouldHaveSize(1) },
                            { dbService.findImageByStatisticIdAndCamType(1L, CameraType.WEBCAM_IBERG).shouldNotBeNull() },
                            { dbService.findImageByStatisticIdAndCamType(1L, CameraType.PANOMAX_IBERG).shouldNotBeNull() }
                    )
                }
    }

}) {
    val wm = WireMockServer(options().port(2345))

    override fun beforeEach(testCase: TestCase) {
        flyway.clean()
        flyway.migrate()
    }

    override fun beforeTest(testCase: TestCase) {
        wm.start()
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        wm.stop()
    }
}