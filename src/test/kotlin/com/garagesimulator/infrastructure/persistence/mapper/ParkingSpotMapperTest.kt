package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.infrastructure.persistence.entity.ParkingSpotEntity
import com.garagesimulator.infrastructure.persistence.entity.SectorEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParkingSpotMapperTest {

    private val sectorMapper: SectorMapper = mockk()
    private val mapper = ParkingSpotMapper(sectorMapper)

    @Test
    fun `deve mapear ParkingSpot de dominio para entidade`() {
        // Arrange
        val sectorDomain = Sector(1L, "A", 10.0, 100)
        val sectorEntity = SectorEntity(1L, "A", 10.0, 100)
        val domain = ParkingSpot(1L, sectorDomain, true)
        every { sectorMapper.toEntity(sectorDomain) } returns sectorEntity

        // Act
        val entity = mapper.toEntity(domain)

        // Assert
        assertEquals(domain.id, entity.id)
        assertEquals(domain.isOccupied, entity.isOccupied)
        assertEquals(sectorEntity, entity.sector)
    }

    @Test
    fun `deve mapear ParkingSpot de entidade para dominio`() {
        // Arrange
        val sectorEntity = SectorEntity(1L, "A", 10.0, 100)
        val sectorDomain = Sector(1L, "A", 10.0, 100)
        val entity = ParkingSpotEntity(1L, sectorEntity, true)
        every { sectorMapper.toDomain(sectorEntity) } returns sectorDomain

        // Act
        val domain = mapper.toDomain(entity)

        // Assert
        assertEquals(entity.id, domain.id)
        assertEquals(entity.isOccupied, domain.isOccupied)
        assertEquals(sectorDomain, domain.sector)
    }
}
