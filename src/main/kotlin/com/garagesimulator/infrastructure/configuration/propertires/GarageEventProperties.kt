package com.garagesimulator.infrastructure.configuration.propertires

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "garage.event")
class GarageEventProperties{
    var url: String = ""
}