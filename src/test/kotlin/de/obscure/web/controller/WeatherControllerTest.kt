package de.obscure.web.controller

import de.obscure.web.db.service.DbService
import de.obscure.web.domain.StatisticInformation
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest.MicronautKotestExtension.getMock
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.time.Instant

@MicronautTest
class WeatherControllerTest(
        private val dbService: DbService,
        @Client("/statistics") val httpClient: RxHttpClient) : StringSpec({

    "empty list" {
        val mock = getMock(dbService)
        every { mock.findByTimestampAfter(any()) } returns emptyList()

        val uri = UriBuilder.of("?since=${Instant.now().epochSecond}").build().toString()
        val result = httpClient.toBlocking().retrieve(HttpRequest.GET<StatisticInformation>(uri), StatisticInformation::class.java)

        result.statistics.shouldBeEmpty()
    }

    "byte array" {
        val mock = getMock(dbService)
        every { mock.findImageByStatisticIdAndCamType(any(), any())?.image  } returns arrayOf(1)

        val uri = UriBuilder.of("/images/1/?cameraType=webcam_iberg&size=normal").build().toString()
        val result = httpClient
                .toBlocking()
                .retrieve(HttpRequest.GET<ByteArray>(uri).accept(MediaType.IMAGE_JPEG), ByteArray::class.java)
        result.shouldBe(byteArrayOf(1))
    }

}) {

    @MockBean(DbService::class)
    fun dbService(): DbService {
        return mockk()
    }

}