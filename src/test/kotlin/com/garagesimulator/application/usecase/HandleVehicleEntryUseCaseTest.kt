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
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

class HandleVehicleEntryUseCaseTest {

    private lateinit var garageRepository: GarageRepositoryPort
    private lateinit var parkingSessionRepository: ParkingSessionRepositoryPort
    private lateinit var handleVehicleEntryUseCase: HandleVehicleEntryUseCase

    @BeforeEach
    fun setUp() {
        garageRepository = mockk(relaxed = true)
        parkingSessionRepository = mockk(relaxed = true)
        every { garageRepository.saveSpot(any()) } answers { invocation.args[0] as ParkingSpot }
        handleVehicleEntryUseCase = HandleVehicleEntryUseCase(garageRepository, parkingSessionRepository)
    }

    @Test
    fun `deve permitir entrada e criar sessao com sucesso`() {
        // Arrange
        val sector = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val spot = ParkingSpot(1L, sector, false, -23.0, -46.0)
        every { garageRepository.getTotalSpotsCount() } returns 100
        every { garageRepository.findSpotByCoordinates(any(), any()) } returns spot

        // Act
        handleVehicleEntryUseCase.execute("ABC-1234", LocalDateTime.now(), -23.0, -46.0)

        // Assert
        verify(exactly = 1) { parkingSessionRepository.save(any()) }
        assertTrue(spot.isOccupied)
    }

    @Test
    fun `deve lancar GarageFullException quando estacionamento estiver lotado`() {
        // Arrange
        every { garageRepository.getTotalSpotsCount() } returns 100
        every { garageRepository.getOccupiedSpotsCount() } returns 100

        // Act & Assert
        assertThrows<GarageFullException> {
            handleVehicleEntryUseCase.execute("ABC-1234", LocalDateTime.now(), -23.0, -46.0)
        }
    }

    @Test
    fun `deve calcular preco com desconto para baixa ocupacao`() {
        // Arrange
        val sector = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val spot = ParkingSpot(1L, sector, false, -23.0, -46.0)
        every { garageRepository.getTotalSpotsCount() } returns 100
        every { garageRepository.getOccupiedSpotsCount() } returns 20 // 20% de ocupação -> -10% de desconto
        every { garageRepository.findSpotByCoordinates(any(), any()) } returns spot

        // Act
        handleVehicleEntryUseCase.execute("ABC-1234", LocalDateTime.now(), -23.0, -46.0)

        // Assert
        verify {
            parkingSessionRepository.save(match { it.dynamicPricePercentage == BigDecimal("-0.10") })
        }
    }

    @Test
    fun `deve calcular preco com acrescimo para alta ocupacao`() {
        // Arrange
        val sector = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val spot = ParkingSpot(1L, sector, false, -23.0, -46.0)
        every { garageRepository.getTotalSpotsCount() } returns 100
        every { garageRepository.getOccupiedSpotsCount() } returns 80 // 80% de ocupação -> +25% de acréscimo
        every { garageRepository.findSpotByCoordinates(any(), any()) } returns spot

        // Act
        handleVehicleEntryUseCase.execute("ABC-1234", LocalDateTime.now(), -23.0, -46.0)

        // Assert
        verify {
            parkingSessionRepository.save(match { it.dynamicPricePercentage == BigDecimal("0.25") })
        }
    }
}
