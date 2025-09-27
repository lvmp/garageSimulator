package com.garagesimulator.application.usecase

import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class HandleVehicleEntryUseCaseTest {

    private lateinit var garageRepository: GarageRepositoryPort
    private lateinit var parkingSessionRepository: ParkingSessionRepositoryPort
    private lateinit var handleVehicleEntryUseCase: HandleVehicleEntryUseCase

    @BeforeEach
    fun setUp() {
        garageRepository = mockk(relaxed = true)
        parkingSessionRepository = mockk(relaxed = true)
        handleVehicleEntryUseCase = HandleVehicleEntryUseCase(garageRepository, parkingSessionRepository)
    }

    @Test
    fun `deve permitir entrada e criar sessao com sucesso`() {
        // Arrange
        val sector = Sector(1L, "A", 10.0, 100)
        val spot = ParkingSpot(1L, sector)
        every { garageRepository.getTotalSpotsCount() } returns 100
        every { garageRepository.getOccupiedSpotsCount() } returns 40 // 40% de ocupação -> preço normal
        every { garageRepository.findAvailableSpotInSector("A") } returns spot

        // Act
        handleVehicleEntryUseCase.execute("ABC-1234", LocalDateTime.now())

        // Assert
        verify(exactly = 1) { parkingSessionRepository.save(any()) }
        verify(exactly = 1) { garageRepository.saveAllSpots(listOf(spot)) }
        assertTrue(spot.isOccupied)
    }

    @Test
    fun `deve lancar GarageFullException quando estacionamento estiver lotado`() {
        // Arrange
        every { garageRepository.getTotalSpotsCount() } returns 100
        every { garageRepository.getOccupiedSpotsCount() } returns 100

        // Act & Assert
        assertThrows<GarageFullException> {
            handleVehicleEntryUseCase.execute("ABC-1234", LocalDateTime.now())
        }
    }

    @Test
    fun `deve calcular preco com desconto para baixa ocupacao`() {
        // Arrange
        val sector = Sector(1L, "A", 10.0, 100)
        val spot = ParkingSpot(1L, sector)
        every { garageRepository.getTotalSpotsCount() } returns 100
        every { garageRepository.getOccupiedSpotsCount() } returns 20 // 20% de ocupação -> -10% de desconto
        every { garageRepository.findAvailableSpotInSector("A") } returns spot

        // Act
        handleVehicleEntryUseCase.execute("ABC-1234", LocalDateTime.now())

        // Assert
        verify {
            parkingSessionRepository.save(match { it.dynamicPricePercentage == -0.10 })
        }
    }

    @Test
    fun `deve calcular preco com acrescimo para alta ocupacao`() {
        // Arrange
        val sector = Sector(1L, "A", 10.0, 100)
        val spot = ParkingSpot(1L, sector)
        every { garageRepository.getTotalSpotsCount() } returns 100
        every { garageRepository.getOccupiedSpotsCount() } returns 80 // 80% de ocupação -> +25% de acréscimo
        every { garageRepository.findAvailableSpotInSector("A") } returns spot

        // Act
        handleVehicleEntryUseCase.execute("ABC-1234", LocalDateTime.now())

        // Assert
        verify {
            parkingSessionRepository.save(match { it.dynamicPricePercentage == 0.25 })
        }
    }
}
