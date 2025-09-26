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
        val sectorA = Sector(0, "A", 10.0, 10)
        val sectorB = Sector(0, "B", 15.0, 5)
        adapter.saveAllSectors(listOf(sectorA, sectorB))

        val spot1A = ParkingSpot(0, sectorA, true) // Ocupada
        val spot2A = ParkingSpot(0, sectorA, false) // Livre
        val spot1B = ParkingSpot(0, sectorB, true) // Ocupada
        adapter.saveAllSpots(listOf(spot1A, spot2A, spot1B))

        // Act
        val availableSpot = adapter.findAvailableSpotInSector("A")
        val totalSpots = adapter.getTotalSpotsCount()
        val occupiedSpots = adapter.getOccupiedSpotsCount()

        // Assert
        assertNotNull(availableSpot)
        assertEquals(false, availableSpot?.isOccupied)
        assertEquals(3, totalSpots)
        assertEquals(2, occupiedSpots)
    }
}
