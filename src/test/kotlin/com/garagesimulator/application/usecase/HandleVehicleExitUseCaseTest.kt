package com.garagesimulator.application.usecase

import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.domain.model.Vehicle
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

class HandleVehicleExitUseCaseTest {

    private lateinit var parkingSessionRepository: ParkingSessionRepositoryPort
    private lateinit var garageRepository: GarageRepositoryPort
    private lateinit var handleVehicleExitUseCase: HandleVehicleExitUseCase

    @BeforeEach
    fun setUp() {
        parkingSessionRepository = mockk(relaxed = true)
        garageRepository = mockk(relaxed = true)
        every { garageRepository.saveSpot(any()) } answers { invocation.args[0] as ParkingSpot }
        handleVehicleExitUseCase = HandleVehicleExitUseCase(parkingSessionRepository, garageRepository)
    }

    @Test
    fun `deve finalizar sessao com sucesso`() {
        // Arrange
        val entryTime = LocalDateTime.now().minusSeconds(3600) // 1 hour ago
        val sector = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val spot = ParkingSpot(1L, sector, isOccupied = true, latitude = -23.0, longitude = -46.0)
        val vehicle = Vehicle("EXIT-001")
        val activeSession = ParkingSession(1L, vehicle, spot, entryTime)

        every { parkingSessionRepository.findActiveByPlate("EXIT-001") } returns activeSession

        // Act
        val exitTime = LocalDateTime.now()
        handleVehicleExitUseCase.execute("EXIT-001", exitTime)

        // Assert
        assertNotNull(activeSession.exitTime)
        assertNotNull(activeSession.finalCost)
        assertFalse(spot.isOccupied)

        verify(exactly = 1) { parkingSessionRepository.save(activeSession) }
        verify(exactly = 1) { garageRepository.saveSpot(spot) }
    }

    @Test
    fun `deve lancar excecao se a sessao nao for encontrada`() {
        // Arrange
        every { parkingSessionRepository.findActiveByPlate(any()) } returns null

        // Act & Assert
        assertThrows<ParkingSessionNotFoundException> {
            handleVehicleExitUseCase.execute("NOT-FOUND", LocalDateTime.now())
        }
    }
}
