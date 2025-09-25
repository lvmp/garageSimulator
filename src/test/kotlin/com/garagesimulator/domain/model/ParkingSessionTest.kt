package com.garagesimulator.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class ParkingSessionTest {

    private lateinit var sector: Sector
    private lateinit var vehicle: Vehicle
    private lateinit var spot: ParkingSpot

    @BeforeEach
    fun setUp() {
        sector = Sector(1L, "A", 10.0, 100)
        vehicle = Vehicle("TEST-001")
        spot = ParkingSpot(1L, sector)
    }

    @Test
    fun `deve lancar excecao ao calcular custo sem tempo de saida`() {
        // Arrange
        val session = ParkingSession(1L, vehicle, spot, Instant.now())

        // Act & Assert
        assertThrows<IllegalStateException> {
            session.calculateCost()
        }
    }

    @Test
    fun `nao deve cobrar nada para sessoes de ate 30 minutos`() {
        // Arrange
        val entryTime = Instant.now()
        val exitTime = entryTime.plusSeconds(29 * 60)
        val session = ParkingSession(1L, vehicle, spot, entryTime, exitTime = exitTime)

        // Act
        session.calculateCost()

        // Assert
        assertEquals(0.0, session.finalCost)
    }

    @Test
    fun `deve cobrar 1 hora para sessoes de 31 minutos`() {
        // Arrange
        val entryTime = Instant.now()
        val exitTime = entryTime.plusSeconds(31 * 60)
        val session = ParkingSession(1L, vehicle, spot, entryTime, exitTime = exitTime)

        // Act
        session.calculateCost()

        // Assert
        assertEquals(10.0, session.finalCost) // 1 * basePrice
    }

    @Test
    fun `deve cobrar 3 horas para sessoes de 2 horas e 15 minutos`() {
        // Arrange
        val entryTime = Instant.now()
        val exitTime = entryTime.plusSeconds((2 * 60 + 15) * 60)
        val session = ParkingSession(1L, vehicle, spot, entryTime, exitTime = exitTime)

        // Act
        session.calculateCost()

        // Assert
        assertEquals(30.0, session.finalCost) // 3 * basePrice
    }

    @Test
    fun `deve aplicar desconto de 10% com preco dinamico`() {
        // Arrange
        val entryTime = Instant.now()
        val exitTime = entryTime.plusSeconds(45 * 60)
        val session = ParkingSession(1L, vehicle, spot, entryTime, exitTime = exitTime, dynamicPricePercentage = -0.10)

        // Act
        session.calculateCost()

        // Assert
        assertEquals(9.0, session.finalCost) // 10.0 * (1 - 0.10)
    }

    @Test
    fun `deve aplicar acrescimo de 25% com preco dinamico`() {
        // Arrange
        val entryTime = Instant.now()
        val exitTime = entryTime.plusSeconds(45 * 60)
        val session = ParkingSession(1L, vehicle, spot, entryTime, exitTime = exitTime, dynamicPricePercentage = 0.25)

        // Act
        session.calculateCost()

        // Assert
        assertEquals(12.5, session.finalCost) // 10.0 * (1 + 0.25)
    }
}
