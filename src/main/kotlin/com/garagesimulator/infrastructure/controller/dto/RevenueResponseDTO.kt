package com.garagesimulator.infrastructure.controller.dto

import java.time.Instant

data class RevenueResponseDTO(
    val amount: Double,
    val currency: String = "BRL",
    val timestamp: Instant
)
