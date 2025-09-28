package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.infrastructure.persistence.entity.ParkingSpotEntity
import com.garagesimulator.infrastructure.persistence.entity.SectorEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalTime

class ParkingSpotMapperTest {

    private val sectorMapper: SectorMapper = mockk()
    private val mapper = ParkingSpotMapper(sectorMapper)

    @Test
    fun `deve mapear ParkingSpot de dominio para entidade`() {
        // Arrange
        val sectorDomain = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val sectorEntity = SectorEntity(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val domain = ParkingSpot(1L, sectorDomain, true, -23.0, -46.0)
        every { sectorMapper.toEntity(sectorDomain) } returns sectorEntity

        // Act
        val entity = mapper.toEntity(domain)

        // Assert
        assertEquals(domain.id, entity.id)
        assertEquals(domain.isOccupied, entity.isOccupied)
        assertEquals(domain.latitude, entity.latitude)
        assertEquals(domain.longitude, entity.longitude)
        assertEquals(sectorEntity, entity.sector)
    }

    @Test
    fun `deve mapear ParkingSpot de entidade para dominio`() {
        // Arrange
        val sectorEntity = SectorEntity(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val sectorDomain = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val entity = ParkingSpotEntity(1L, sectorEntity, true, -23.0, -46.0)
        every { sectorMapper.toDomain(sectorEntity) } returns sectorDomain

        // Act
        val domain = mapper.toDomain(entity)

        // Assert
        assertEquals(entity.id, domain.id)
        assertEquals(entity.isOccupied, domain.isOccupied)
        assertEquals(entity.latitude, domain.latitude)
        assertEquals(entity.longitude, domain.longitude)
        assertEquals(sectorDomain, domain.sector)
    }
}
