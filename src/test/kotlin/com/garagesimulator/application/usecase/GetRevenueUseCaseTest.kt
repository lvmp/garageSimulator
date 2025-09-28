package com.garagesimulator.application.usecase

import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.domain.model.Vehicle
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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
            createMockSession(BigDecimal("10.0")),
            createMockSession(BigDecimal("15.5")),
            createMockSession(BigDecimal("20.0"))
        )
        every { parkingSessionRepository.findFinishedByDateAndSector(date, sectorName) } returns sessions

        // Act
        val totalRevenue = getRevenueUseCase.execute(date, sectorName)

        // Assert
        assertEquals(BigDecimal("45.5"), totalRevenue)
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
        assertEquals(BigDecimal.ZERO, totalRevenue)
    }

    private fun createMockSession(cost: BigDecimal): ParkingSession {
        val sector = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val spot = ParkingSpot(1L, sector, false, -23.0, -46.0)
        val vehicle = Vehicle("MOCK-001")
        return ParkingSession(1L, vehicle, spot, LocalDateTime.now(), finalCost = cost)
    }
}
