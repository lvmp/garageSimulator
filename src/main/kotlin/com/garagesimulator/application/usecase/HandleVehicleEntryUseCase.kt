package com.garagesimulator.application.usecase

import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.exception.NoAvailableSpotException
import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.domain.model.Vehicle
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class HandleVehicleEntryUseCase(
    private val garageRepository: GarageRepositoryPort,
    private val parkingSessionRepository: ParkingSessionRepositoryPort,
) {

    private val logger = LoggerFactory.getLogger(HandleVehicleEntryUseCase::class.java)

    fun execute(licensePlate: String, entryTime: LocalDateTime) {
        logger.info("Processando entrada de veículo: {}", licensePlate)
        val occupiedSpots = garageRepository.getOccupiedSpotsCount()
        val totalSpots = garageRepository.getTotalSpotsCount()

        if (totalSpots > 0 && occupiedSpots >= totalSpots) {
            logger.warn("Estacionamento lotado para entrada de {}.", licensePlate)
            throw GarageFullException()
        }

        val occupancyRate = if (totalSpots > 0) occupiedSpots.toDouble() / totalSpots else 0.0
        val dynamicPricePercentage = when {
            occupancyRate < 0.25 -> -0.10 // -10% de desconto
            occupancyRate < 0.50 -> 0.0   // Preço normal
            occupancyRate < 0.75 -> 0.10  // +10% de acréscimo
            else -> 0.25                  // +25% de acréscimo
        }

        val availableSpot = garageRepository.findAvailableSpot() ?: run {
            logger.warn("Nenhuma vaga disponível para entrada de {}.", licensePlate)
            throw NoAvailableSpotException()
        }

        val vehicle = Vehicle(licensePlate)
        val session = ParkingSession(
            id = 0, // O ID será gerado pela persistência
            vehicle = vehicle,
            parkingSpot = availableSpot,
            entryTime = entryTime,
            dynamicPricePercentage = dynamicPricePercentage
        )

        availableSpot.occupy()
        garageRepository.saveSpot(availableSpot)
        parkingSessionRepository.save(session)
        logger.info("Veículo {} entrou com sucesso na vaga {}.", licensePlate, availableSpot.id)
    }
}
