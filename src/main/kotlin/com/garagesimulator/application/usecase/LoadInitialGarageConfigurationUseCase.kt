package com.garagesimulator.application.usecase

import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.SimulatorClientPort
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import org.springframework.stereotype.Component

@Component
class LoadInitialGarageConfigurationUseCase(
    private val simulatorClient: SimulatorClientPort,
    private val garageRepository: GarageRepositoryPort
) {

    fun execute() {
        val garageConfig = simulatorClient.getGarageConfiguration()

        val sectors = garageConfig.garage.map { dto ->
            Sector(id = 0, name = dto.sector, basePrice = dto.basePrice, maxCapacity = dto.max_capacity)
        }
        garageRepository.saveAllSectors(sectors)

        val spots = garageConfig.spots.map { dto ->
            // Encontra o setor correspondente pelo nome
            val sector = sectors.first { it.name == dto.sector }
            ParkingSpot(id = 0, sector = sector, isOccupied = false)
        }
        garageRepository.saveAllSpots(spots)
    }
}
