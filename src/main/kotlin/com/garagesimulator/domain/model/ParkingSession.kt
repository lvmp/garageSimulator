package com.garagesimulator.domain.model

import java.time.Duration
import java.time.Instant
import kotlin.math.ceil

/**
 * Entidade central que representa a sessão de um veículo no estacionamento.
 * Pattern: Aggregate Root, pois gerencia o ciclo de vida de uma estadia.
 * Princípio SOLID: SRP e OCP (no cálculo de custo, que é extensível).
 */
data class ParkingSession(
    val id: Long,
    val vehicle: Vehicle,
    val parkingSpot: ParkingSpot,
    val entryTime: Instant,
    var exitTime: Instant? = null,
    val dynamicPricePercentage: Double = 0.0, // Ex: 0.1 para +10%, -0.1 para -10%
    var finalCost: Double? = null,
) {

    fun calculateCost() {
        check(exitTime != null) { "O custo só pode ser calculado após a saída do veículo." }

        val durationInMinutes = Duration.between(entryTime, exitTime).toMinutes()

        if (durationInMinutes <= 30) {
            finalCost = 0.0
            return
        }

        val hours = ceil(durationInMinutes / 60.0).toInt()
        val baseCost = hours * parkingSpot.sector.basePrice
        val adjustedCost = baseCost * (1 + dynamicPricePercentage)

        finalCost = adjustedCost
    }
}
