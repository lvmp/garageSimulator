package com.garagesimulator.domain.model

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
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
    val entryTime: LocalDateTime,
    var exitTime: LocalDateTime? = null,
    val dynamicPricePercentage: BigDecimal? = BigDecimal.ZERO, // Ex: 0.1 para +10%, -0.1 para -10%
    var finalCost: BigDecimal? = BigDecimal.ZERO,
) {

    fun calculateCost() {
        check(exitTime != null) { "O custo só pode ser calculado após a saída do veículo." }

        val durationInMinutes = Duration.between(entryTime, exitTime).toMinutes()

        if (durationInMinutes <= 30) {
            finalCost = BigDecimal.ZERO
            return
        }

        val hours = ceil(durationInMinutes.toDouble() / 60.0).toInt()
        val baseCost = parkingSpot.sector.basePrice.multiply(BigDecimal(hours))
        val adjustedCost = baseCost.multiply(BigDecimal.ONE.add(dynamicPricePercentage))

        finalCost = adjustedCost
    }
}
