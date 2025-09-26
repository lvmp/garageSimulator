package com.garagesimulator.infrastructure.controller.dto

import java.time.Instant

data class WebhookEventDTO(
    val license_plate: String,
    val entry_time: Instant? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val exit_time: Instant? = null,
    val event_type: String
)
