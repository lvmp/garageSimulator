package com.garagesimulator.infrastructure.controller.dto

import java.math.BigDecimal
import java.time.Instant

data class RevenueResponseDTO(
    val amount: BigDecimal,
    val currency: String = "BRL",
    val timestamp: Instant
)
