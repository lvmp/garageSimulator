package com.garagesimulator.domain.model

/**
 * Representa um setor da garagem com uma tarifa base e capacidade.
 * Pattern: Entity.
 */
data class Sector(
    val id: Long,
    val name: String,
    val basePrice: Double,
    val maxCapacity: Int,
)
