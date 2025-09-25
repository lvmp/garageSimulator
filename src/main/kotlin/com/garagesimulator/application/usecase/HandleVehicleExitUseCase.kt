package com.garagesimulator.application.usecase

import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import java.time.Instant

/**
 * Caso de Uso para tratar a saída de um veículo.
 * Pattern: Command
 * Princípio SOLID: SRP
 */
class HandleVehicleExitUseCase(
    private val parkingSessionRepository: ParkingSessionRepositoryPort,
    private val garageRepository: GarageRepositoryPort,
) {

    fun execute(licensePlate: String, exitTime: Instant) {
        val session = parkingSessionRepository.findActiveByPlate(licensePlate)
            ?: throw ParkingSessionNotFoundException(licensePlate)

        session.exitTime = exitTime
        session.calculateCost()

        val spot = session.parkingSpot
        spot.vacate()

        parkingSessionRepository.save(session)
        garageRepository.saveAllSpots(listOf(spot))
    }
}
