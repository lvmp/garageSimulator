package com.garagesimulator.application.usecase

import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.SimulatorClientPort
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.infrastructure.controller.dto.GarageConfigDTO
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
            val sectors = saveSectors(garageConfig)
            saveSpots(garageConfig, sectors)

            logger.info("Configuração inicial da garagem carregada com sucesso.")
        } catch (e: Exception) {
            logger.error("Erro ao carregar configuração inicial da garagem: {}", e.message, e)
            throw IllegalStateException("Falha ao carregar configuração inicial da garagem.", e)
        }
    }

    private fun saveSpots(
        garageConfig: GarageConfigDTO,
        sectors: List<Sector>
    ) {
        val spotsToSave = garageConfig.spots.mapNotNull { dto ->
            val sector = sectors.first { it.name == dto.sector }

            // valida se já existe
            val existing = garageRepository.findSpotByCoordinates(dto.lat, dto.lng)
            if (existing == null) {
                ParkingSpot(
                    id = 0,
                    sector = sector,
                    isOccupied = dto.occupied,
                    latitude = dto.lat,
                    longitude = dto.lng
                )
            } else {
                null
            }
        }

        if (spotsToSave.isNotEmpty()) {
            garageRepository.saveAllSpots(spotsToSave)
            logger.info("{} novas vagas persistidas.", spotsToSave.size)
        } else {
            logger.info("Nenhuma nova vaga para persistir.")
        }
    }

    private fun saveSectors(garageConfig: GarageConfigDTO): List<Sector> {
        val sectors = garageConfig.garage.map { dto ->
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

        // busca setores já existentes no banco
        val existingSectors = garageRepository.findAllSectors()
        val sectorsToSave = sectors.filter { s ->
            existingSectors.none { it.name == s.name }
        }

        if (sectorsToSave.isNotEmpty()) {
            garageRepository.saveAllSectors(sectorsToSave)
            logger.info("{} novos setores persistidos.", sectorsToSave.size)
        } else {
            logger.info("Nenhum novo setor para persistir.")
        }
        return sectors
    }
}
