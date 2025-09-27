package com.garagesimulator.infrastructure.configuration

import com.garagesimulator.infrastructure.configuration.propertires.GarageEventProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig(val garageEvent: GarageEventProperties) {

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder().baseUrl(garageEvent.url).build()
    }
}
