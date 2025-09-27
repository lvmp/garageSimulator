package com.garagesimulator.application.usecase

import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import org.slf4j.LoggerFactory
import java.time.LocalDate

class GetRevenueUseCase(
    private val parkingSessionRepository: ParkingSessionRepositoryPort,
) {

    private val logger = LoggerFactory.getLogger(GetRevenueUseCase::class.java)

    fun execute(date: LocalDate, sectorName: String): Double {
        logger.info("Calculando receita para data {} e setor {}", date, sectorName)
        val finishedSessions = parkingSessionRepository.findFinishedByDateAndSector(date, sectorName)
        val totalRevenue = finishedSessions.sumOf { it.finalCost ?: 0.0 }
        logger.info("Receita total para data {} e setor {}: {}", date, sectorName, totalRevenue)
        return totalRevenue
    }
}
