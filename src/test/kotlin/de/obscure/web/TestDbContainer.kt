package de.obscure.web

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class TestDbContainer : PostgreSQLContainer<TestDbContainer>(DockerImageName.parse("postgres:13")) {
    companion object {
        private lateinit var instance: TestDbContainer

        fun start() {
            if (!Companion::instance.isInitialized) {
                instance = TestDbContainer()

                instance.start() // At this point we have a running PostgreSQL instance as a Docker container

                // We set the properties below, so Micronaut will use these when it starts
                System.setProperty("datasources.default.url", instance.jdbcUrl)
                System.setProperty("datasources.default.username", instance.username)
                System.setProperty("datasources.default.password", instance.password)
            }
        }

        fun stop() {
            if (Companion::instance.isInitialized) {
                instance.stop()
            }
        }
    }
}