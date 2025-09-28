package com.garagesimulator.domain.model

/**
 * Representa um setor da garagem com uma tarifa base e capacidade.
 * Pattern: Entity.
 */
import java.math.BigDecimal
import java.time.LocalTime

data class Sector(
    val id: Long,
    val name: String,
    val basePrice: BigDecimal,
    val maxCapacity: Int,
    val openHour: LocalTime,
    val closeHour: LocalTime,
    val durationLimitMinutes: Int,
)
