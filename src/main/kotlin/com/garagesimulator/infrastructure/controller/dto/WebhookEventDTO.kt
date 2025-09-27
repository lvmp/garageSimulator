package com.garagesimulator.infrastructure.controller.dto

import java.time.LocalDateTime

data class WebhookEventDTO(
    val license_plate: String,
    val entry_time: LocalDateTime? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val exit_time: LocalDateTime? = null,
    val event_type: String
)
