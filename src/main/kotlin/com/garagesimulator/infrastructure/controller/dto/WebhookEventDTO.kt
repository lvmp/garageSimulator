package com.garagesimulator.infrastructure.controller.dto

import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

data class WebhookEventDTO(
    @field:Pattern(
        regexp = "^[A-Z]{3}-?[0-9][A-Z0-9][0-9]{2}$",
        message = "Field 'license_plate' is not in a valid format."
    )
    val license_plate: String,
    val entry_time: LocalDateTime? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val exit_time: LocalDateTime? = null,
    val event_type: String
)
