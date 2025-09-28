package com.garagesimulator.application.usecase

import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.SimulatorClientPort
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalTime

@Component
class LoadInitialGarageConfigurationUseCase(
    private val simulatorClient: SimulatorClientPort,
    private val garageRepository: GarageRepositoryPort
) {

    private val logger = LoggerFactory.getLogger(LoadInitialGarageConfigurationUseCase::class.java)

    fun execute() {
        logger.info("Iniciando carregamento da configuração inicial da garagem.")
        try {
            val garageConfig = simulatorClient.getGarageConfiguration()

            val sectors = garageConfig.garage.map { dto ->
                garageRepository.

                Sector(
                    id = 0,
                    name = dto.sector,
                    basePrice = BigDecimal(dto.base_price.toString()),
                    maxCapacity = dto.max_capacity,
                    openHour = LocalTime.parse(dto.open_hour),
                    closeHour = LocalTime.parse(dto.close_hour),
                    durationLimitMinutes = dto.duration_limit_minutes
                )
            }

            val savedSectors = garageRepository.saveAllSectors(sectors)
            logger.info("{} setores carregados e persistidos.", savedSectors.size)

            val spots = garageConfig.spots.map { dto ->
                val sector = savedSectors.first { it.name == dto.sector }
                ParkingSpot(
                    id = 0,
                    sector = sector,
                    isOccupied = dto.occupied,
                    latitude = dto.lat,
                    longitude = dto.lng
                )
            }
            garageRepository.saveAllSpots(spots)
            logger.info("{} vagas carregadas e persistidas.", spots.size)
            logger.info("Configuração inicial da garagem carregada com sucesso.")
        } catch (e: Exception) {
            logger.error("Erro ao carregar configuração inicial da garagem: {}", e.message, e)
            throw IllegalStateException("Falha ao carregar configuração inicial da garagem.", e)
        }
    }
}
