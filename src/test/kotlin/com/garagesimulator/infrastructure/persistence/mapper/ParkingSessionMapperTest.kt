package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.domain.model.Vehicle
import com.garagesimulator.infrastructure.persistence.entity.ParkingSessionEntity
import com.garagesimulator.infrastructure.persistence.entity.ParkingSpotEntity
import com.garagesimulator.infrastructure.persistence.entity.SectorEntity
import com.garagesimulator.infrastructure.persistence.entity.VehicleEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

class ParkingSessionMapperTest {

    private val vehicleMapper: VehicleMapper = mockk()
    private val parkingSpotMapper: ParkingSpotMapper = mockk()
    private val mapper = ParkingSessionMapper(vehicleMapper, parkingSpotMapper)

    @Test
    fun `deve mapear ParkingSession de dominio para entidade`() {
        // Arrange
        val vehicleDomain = Vehicle("ABC-123")
        val vehicleEntity = VehicleEntity("ABC-123")
        val sectorDomain = Sector(1L, "A", 10.0, 100)
        val spotDomain = ParkingSpot(1L, sectorDomain, true)
        val spotEntity = ParkingSpotEntity(1L, SectorEntity(1L, "A", 10.0, 100), true)
        val entryTime = Instant.now()
        val exitTime = Instant.now().plusSeconds(3600)
        val domain = ParkingSession(1L, vehicleDomain, spotDomain, entryTime, exitTime, 0.1, 11.0)

        every { vehicleMapper.toEntity(vehicleDomain) } returns vehicleEntity
        every { parkingSpotMapper.toEntity(spotDomain) } returns spotEntity

        // Act
        val entity = mapper.toEntity(domain)

        // Assert
        assertEquals(domain.id, entity.id)
        assertEquals(vehicleEntity, entity.vehicle)
        assertEquals(spotEntity, entity.parkingSpot)
        assertEquals(domain.entryTime, entity.entryTime)
        assertEquals(domain.exitTime, entity.exitTime)
        assertEquals(domain.dynamicPricePercentage, entity.dynamicPricePercentage)
        assertEquals(domain.finalCost, entity.finalCost)
    }

    @Test
    fun `deve mapear ParkingSession de entidade para dominio`() {
        // Arrange
        val vehicleEntity = VehicleEntity("ABC-123")
        val vehicleDomain = Vehicle("ABC-123")
        val sectorEntity = SectorEntity(1L, "A", 10.0, 100)
        val spotEntity = ParkingSpotEntity(1L, sectorEntity, true)
        val spotDomain = ParkingSpot(1L, Sector(1L, "A", 10.0, 100), true)
        val entryTime = Instant.now()
        val exitTime = Instant.now().plusSeconds(3600)
        val entity = ParkingSessionEntity(1L, vehicleEntity, spotEntity, entryTime, exitTime, 0.1, 11.0)

        every { vehicleMapper.toDomain(vehicleEntity) } returns vehicleDomain
        every { parkingSpotMapper.toDomain(spotEntity) } returns spotDomain

        // Act
        val domain = mapper.toDomain(entity)

        // Assert
        assertEquals(entity.id, domain.id)
        assertEquals(vehicleDomain, domain.vehicle)
        assertEquals(spotDomain, domain.parkingSpot)
        assertEquals(entity.entryTime, domain.entryTime)
        assertEquals(entity.exitTime, domain.exitTime)
        assertEquals(entity.dynamicPricePercentage, domain.dynamicPricePercentage)
        assertEquals(entity.finalCost, domain.finalCost)
    }
}
