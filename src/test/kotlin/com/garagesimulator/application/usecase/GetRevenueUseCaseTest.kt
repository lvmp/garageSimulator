package com.garagesimulator.application.usecase

import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.domain.model.Vehicle
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

class GetRevenueUseCaseTest {

    private lateinit var parkingSessionRepository: ParkingSessionRepositoryPort
    private lateinit var getRevenueUseCase: GetRevenueUseCase

    @BeforeEach
    fun setUp() {
        parkingSessionRepository = mockk()
        getRevenueUseCase = GetRevenueUseCase(parkingSessionRepository)
    }

    @Test
    fun `deve retornar a soma dos custos das sessoes finalizadas`() {
        // Arrange
        val date = LocalDate.now()
        val sectorName = "A"
        val sessions = listOf(
            createMockSession(10.0),
            createMockSession(15.5),
            createMockSession(20.0)
        )
        every { parkingSessionRepository.findFinishedByDateAndSector(date, sectorName) } returns sessions

        // Act
        val totalRevenue = getRevenueUseCase.execute(date, sectorName)

        // Assert
        assertEquals(45.5, totalRevenue)
    }

    @Test
    fun `deve retornar zero se nao houver sessoes finalizadas`() {
        // Arrange
        val date = LocalDate.now()
        val sectorName = "A"
        every { parkingSessionRepository.findFinishedByDateAndSector(date, sectorName) } returns emptyList()

        // Act
        val totalRevenue = getRevenueUseCase.execute(date, sectorName)

        // Assert
        assertEquals(0.0, totalRevenue)
    }

    private fun createMockSession(cost: Double): ParkingSession {
        val sector = Sector(1L, "A", 10.0, 100)
        val spot = ParkingSpot(1L, sector)
        val vehicle = Vehicle("MOCK-001")
        return ParkingSession(1L, vehicle, spot, Instant.now(), finalCost = cost)
    }
}
