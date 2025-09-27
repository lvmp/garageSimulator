package com.garagesimulator.application.usecase

import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class HandleVehicleExitUseCase(
    private val parkingSessionRepository: ParkingSessionRepositoryPort,
    private val garageRepository: GarageRepositoryPort,
) {

    private val logger = LoggerFactory.getLogger(HandleVehicleExitUseCase::class.java)

    fun execute(licensePlate: String, exitTime: LocalDateTime) {
        logger.info("Processando saída de veículo: {}", licensePlate)
        val session = parkingSessionRepository.findActiveByPlate(licensePlate)
            ?: run {
                logger.warn("Sessão não encontrada para saída de {}.", licensePlate)
                throw ParkingSessionNotFoundException(licensePlate)
            }

        session.exitTime = exitTime
        session.calculateCost()

        val spot = session.parkingSpot
        spot.vacate()

        parkingSessionRepository.save(session)
        garageRepository.saveAllSpots(listOf(spot))
        logger.info("Veículo {} saiu com sucesso. Custo final: {}", licensePlate, session.finalCost)
    }
}
