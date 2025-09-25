package com.garagesimulator.application.usecase

import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import java.time.LocalDate

/**
 * Caso de Uso para consultar o faturamento.
 * Pattern: Query
 * Princípio SOLID: SRP
 */
class GetRevenueUseCase(
    private val parkingSessionRepository: ParkingSessionRepositoryPort,
) {

    fun execute(date: LocalDate, sectorName: String): Double {
        val finishedSessions = parkingSessionRepository.findFinishedByDateAndSector(date, sectorName)

        return finishedSessions.sumOf { it.finalCost ?: 0.0 }
    }
}
