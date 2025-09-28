package com.garagesimulator.infrastructure.persistence.repository

import com.garagesimulator.infrastructure.gateway.GarageRepositoryGateway

import com.garagesimulator.infrastructure.mapper.ParkingSpotMapper
import com.garagesimulator.infrastructure.mapper.SectorMapper
import com.garagesimulator.infrastructure.mapper.VehicleMapper
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.time.LocalTime

@DataJpaTest
@Import(
    GarageRepositoryGateway::class,
    ParkingSpotMapper::class,
    SectorMapper::class,
    VehicleMapper::class
)
class GarageRepositoryGatewayTest {

    @Autowired
    private lateinit var adapter: GarageRepositoryGateway

    @Test
    fun `deve salvar e encontrar vaga disponivel`() {
        // Arrange
        val sectorA = Sector(0, "A", BigDecimal("10.0"), 10, LocalTime.MIN, LocalTime.MAX, 1440)
        val sectorB = Sector(0, "B", BigDecimal("15.0"), 5, LocalTime.MIN, LocalTime.MAX, 1440)
        val savedSectors = adapter.saveAllSectors(listOf(sectorA, sectorB))
        val persistedSectorA = savedSectors.first { it.name == "A" }
        val persistedSectorB = savedSectors.first { it.name == "B" }

        val spot1A = ParkingSpot(0, persistedSectorA, true, -23.0, -46.0) // Ocupada
        val spot2A = ParkingSpot(0, persistedSectorA, false, -23.0, -46.0) // Livre
        val spot1B = ParkingSpot(0, persistedSectorB, true, -23.0, -46.0) // Ocupada
        adapter.saveAllSpots(listOf(spot1A, spot2A, spot1B))

        // Act
        val availableSpot = adapter.findAvailableSpot()
        val totalSpots = adapter.getTotalSpotsCount()
        val occupiedSpots = adapter.getOccupiedSpotsCount()

        // Assert
        assertNotNull(availableSpot)
        assertEquals(false, availableSpot?.isOccupied)
        assertEquals("A", availableSpot?.sector?.name) // Garante que é a vaga do setor A
        assertEquals(3, totalSpots)
        assertEquals(2, occupiedSpots)
    }
}
