package com.garagesimulator.application.usecase

import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.SimulatorClientPort
import com.garagesimulator.infrastructure.controller.dto.GarageConfigDTO
import com.garagesimulator.infrastructure.controller.dto.SectorDTO
import com.garagesimulator.infrastructure.controller.dto.SpotDTO
import com.garagesimulator.domain.model.Sector
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoadInitialGarageConfigurationUseCaseTest {

    private lateinit var simulatorClient: SimulatorClientPort
    private lateinit var garageRepository: GarageRepositoryPort
    private lateinit var useCase: LoadInitialGarageConfigurationUseCase

    @BeforeEach
    fun setUp() {
        simulatorClient = mockk()
        garageRepository = mockk(relaxed = true)
        useCase = LoadInitialGarageConfigurationUseCase(simulatorClient, garageRepository)
    }

    @Test
    fun `deve carregar e persistir configuracao da garagem com sucesso`() {
        // Arrange
        val garageConfig = GarageConfigDTO(
            garage = listOf(SectorDTO("A", 10.0, 100)),
            spots = listOf(SpotDTO(1L, "A", -23.0, -46.0))
        )
        every { simulatorClient.getGarageConfiguration() } returns garageConfig
        every { garageRepository.saveAllSectors(any()) } answers { invocation.args[0] as List<Sector> }

        // Act
        useCase.execute()

        // Assert
        verify(exactly = 1) { garageRepository.saveAllSectors(any()) }
        verify(exactly = 1) { garageRepository.saveAllSpots(any()) }
    }
}
