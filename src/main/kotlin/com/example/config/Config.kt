package com.example.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.r2dbc.core.DatabaseClient

@Configuration
@EnableR2dbcAuditing
class Config {

    @Bean
    fun createConnectionFactory(): ConnectionFactory {

        val url = "r2dbc:postgresql://localhost:5432/EmployeeGraphQL"
        val username = "zeerakzubair"

        return ConnectionFactoryBuilder
            .withUrl(url)
            .username(username)
            .build()
    }

    @Bean
    fun databaseClient(connectionFactory: ConnectionFactory): DatabaseClient {
        return DatabaseClient.create(connectionFactory)
    }
}