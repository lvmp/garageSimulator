package com.garagesimulator.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalTime

class ParkingSpotTest {

    private lateinit var sector: Sector

    @BeforeEach
    fun setUp() {
        sector = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
    }

    @Test
    fun `deve ocupar uma vaga livre`() {
        // Arrange
        val spot = ParkingSpot(1L, sector, isOccupied = false, latitude = -23.0, longitude = -46.0)

        // Act
        spot.occupy()

        // Assert
        assertTrue(spot.isOccupied)
    }

    @Test
    fun `deve liberar uma vaga ocupada`() {
        // Arrange
        val spot = ParkingSpot(1L, sector, isOccupied = true, latitude = -23.0, longitude = -46.0)

        // Act
        spot.vacate()

        // Assert
        assertFalse(spot.isOccupied)
    }

    @Test
    fun `deve lancar excecao ao tentar ocupar uma vaga ja ocupada`() {
        // Arrange
        val spot = ParkingSpot(1L, sector, isOccupied = true, latitude = -23.0, longitude = -46.0)

        // Act & Assert
        assertThrows<IllegalStateException> {
            spot.occupy()
        }
    }
}
