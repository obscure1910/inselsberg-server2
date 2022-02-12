package de.obscure.web.http

import de.obscure.web.domain.Statistic
import io.micronaut.http.HttpRequest.GET
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import javax.inject.Singleton

@Singleton
class StatisticWebsite(
        @Client
        val httpClient: RxHttpClient,
        val inselsbergHttpConf: InselsbergHttpConfiguration) {

    fun requestForData(): Flowable<Statistic> {
        LOG.info("Load statistic from ${inselsbergHttpConf.itk.paths.data}")
        return httpClient.retrieve(GET<String>(inselsbergHttpConf.itk.paths.data), ByteArray::class.java)
                .map { rawData ->
                    val str = String(rawData, Charset.forName("windows-1252"))
                    val parser = DataParser(str)
                    Statistic(
                            null,
                            parser.timestampOfData(),
                            parser.temperature(),
                            parser.humidity(),
                            parser.airPressure(),
                            parser.airPressureTrend(),
                            parser.windVelocity(),
                            parser.windGust(),
                            parser.rain(),
                            parser.rainLastHours(),
                            parser.isLiftOpen()
                    )
                }
    }

    companion object {
        val LOG = LoggerFactory.getLogger(StatisticWebsite::class.java)
    }

}