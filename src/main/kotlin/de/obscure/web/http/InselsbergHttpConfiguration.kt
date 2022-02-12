package de.obscure.web.http

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("inselsberg")
interface InselsbergHttpConfiguration {

    val itk: Itk
    val panomax: Panomax

    @ConfigurationProperties("itk")
    interface Itk {
        val host: String
        val port: Int
        val paths: Paths

        @ConfigurationProperties("paths")
        interface Paths {
            val data: String
            val iberg_normal: String
            val tabarz_normal: String
        }
    }

    @ConfigurationProperties("panomax")
    interface Panomax {
        val host: String
        val port: Int
        val paths: Paths

        @ConfigurationProperties("paths")
        interface Paths {
            val iberg_panomax: String
        }
    }

}