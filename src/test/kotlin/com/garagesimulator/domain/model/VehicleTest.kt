package com.garagesimulator.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class VehicleTest {

    @Test
    fun `deve criar veiculo com placa valida`() {
        // Arrange & Act
        val vehicle = Vehicle("ABC-1234")

        // Assert
        assertEquals("ABC-1234", vehicle.licensePlate)
    }

    @Test
    fun `deve lancar excecao ao criar veiculo com placa em branco`() {
        // Arrange, Act & Assert
        assertThrows<IllegalArgumentException> {
            Vehicle("  ")
        }
    }
}
