package com.garagesimulator.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ParkingSpotTest {

    private lateinit var sector: Sector

    @BeforeEach
    fun setUp() {
        sector = Sector(1L, "A", 10.0, 100)
    }

    @Test
    fun `deve ocupar uma vaga livre`() {
        // Arrange
        val spot = ParkingSpot(1L, sector, isOccupied = false)

        // Act
        spot.occupy()

        // Assert
        assertTrue(spot.isOccupied)
    }

    @Test
    fun `deve liberar uma vaga ocupada`() {
        // Arrange
        val spot = ParkingSpot(1L, sector, isOccupied = true)

        // Act
        spot.vacate()

        // Assert
        assertFalse(spot.isOccupied)
    }

    @Test
    fun `deve lancar excecao ao tentar ocupar uma vaga ja ocupada`() {
        // Arrange
        val spot = ParkingSpot(1L, sector, isOccupied = true)

        // Act & Assert
        assertThrows<IllegalStateException> {
            spot.occupy()
        }
    }
}
