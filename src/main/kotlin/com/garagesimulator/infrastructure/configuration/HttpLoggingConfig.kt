package com.garagesimulator.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
class HttpLoggingConfig {

    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter {
        return CommonsRequestLoggingFilter().apply {
            setIncludeQueryString(true)
            setIncludePayload(true)
            setIncludeHeaders(true)
            setMaxPayloadLength(10000)
            setAfterMessagePrefix("📥 Incoming Request: ")
        }
    }
}
