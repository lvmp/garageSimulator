package com.garagesimulator.application.usecase

import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.exception.NoAvailableSpotException
import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.domain.model.Vehicle
import java.time.Instant

/**
 * Caso de Uso para tratar a entrada de um veículo.
 * Pattern: Command
 * Princípio SOLID: SRP
 */
class HandleVehicleEntryUseCase(
    private val garageRepository: GarageRepositoryPort,
    private val parkingSessionRepository: ParkingSessionRepositoryPort,
) {

    fun execute(licensePlate: String, entryTime: Instant) {
        val occupiedSpots = garageRepository.getOccupiedSpotsCount()
        val totalSpots = garageRepository.getTotalSpotsCount()

        if (totalSpots > 0 && occupiedSpots >= totalSpots) {
            throw GarageFullException()
        }

        val occupancyRate = if (totalSpots > 0) occupiedSpots.toDouble() / totalSpots else 0.0
        val dynamicPricePercentage = when {
            occupancyRate < 0.25 -> -0.10 // -10% de desconto
            occupancyRate < 0.50 -> 0.0   // Preço normal
            occupancyRate < 0.75 -> 0.10  // +10% de acréscimo
            else -> 0.25                  // +25% de acréscimo
        }

        // Simplificação: assume que todos os veículos entram no setor "A"
        val availableSpot = garageRepository.findAvailableSpotInSector("A") ?: throw NoAvailableSpotException("A")

        val vehicle = Vehicle(licensePlate)
        val session = ParkingSession(
            id = 0, // O ID será gerado pela persistência
            vehicle = vehicle,
            parkingSpot = availableSpot,
            entryTime = entryTime,
            dynamicPricePercentage = dynamicPricePercentage
        )

        availableSpot.occupy()
        garageRepository.saveAllSpots(listOf(availableSpot)) // Salva o estado da vaga
        parkingSessionRepository.save(session)
    }
}
