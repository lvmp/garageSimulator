package com.garagesimulator.domain.model

/**
 * Representa uma vaga de estacionamento individual dentro de um setor.
 * Pattern: Entity.
 * Princípio SOLID: SRP - Sua única responsabilidade é gerenciar o estado de uma vaga.
 */
data class ParkingSpot(
    val id: Long,
    val sector: Sector,
    var isOccupied: Boolean = false,
    val latitude: Double,
    val longitude: Double,
) {
    fun occupy() {
        check(!isOccupied) { "A vaga já está ocupada." }
        isOccupied = true
    }

    fun vacate() {
        isOccupied = false
    }
}
