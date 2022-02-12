package de.obscure.web.http

import io.micronaut.http.HttpRequest.GET
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.reactivex.Flowable
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class RemotePicture(
        @Client
        val httpClient: RxHttpClient,
        val inselsbergHttpConf: InselsbergHttpConfiguration) {

    fun inselsberg(): Flowable<ByteArray> {
        LOG.info("Load picture from ${inselsbergHttpConf.itk.paths.iberg_normal}")
        return httpClient.retrieve(GET<Any>(inselsbergHttpConf.itk.paths.iberg_normal), ByteArray::class.java)
    }

    fun tabarz(): Flowable<ByteArray> {
        LOG.info("Load picture from ${inselsbergHttpConf.itk.paths.tabarz_normal}")
        return httpClient.retrieve(GET<Any>(inselsbergHttpConf.itk.paths.tabarz_normal), ByteArray::class.java)
    }

    fun panomax(): Flowable<ByteArray> {
        LOG.info("Load picture from ${inselsbergHttpConf.panomax.paths.iberg_panomax}")
        return httpClient.retrieve(GET<Any>(inselsbergHttpConf.panomax.paths.iberg_panomax), ByteArray::class.java)
    }

    companion object {
        val LOG = LoggerFactory.getLogger(RemotePicture::class.java)
    }

}