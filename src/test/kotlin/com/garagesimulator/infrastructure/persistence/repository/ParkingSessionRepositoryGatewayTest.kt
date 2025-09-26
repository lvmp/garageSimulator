package com.garagesimulator.infrastructure.persistence.repository

import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.domain.model.Vehicle
import com.garagesimulator.infrastructure.mapper.ParkingSessionMapper
import com.garagesimulator.infrastructure.mapper.ParkingSpotMapper
import com.garagesimulator.infrastructure.mapper.SectorMapper
import com.garagesimulator.infrastructure.mapper.VehicleMapper
import com.garagesimulator.infrastructure.persistence.repository.ParkingSessionJpaRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import java.time.Instant

import com.garagesimulator.infrastructure.gateway.ParkingSessionRepositoryGateway

@DataJpaTest
@Import(
    ParkingSessionRepositoryGateway::class,
    ParkingSessionMapper::class,
    VehicleMapper::class,
    ParkingSpotMapper::class,
    SectorMapper::class
)
class ParkingSessionRepositoryAdapterTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var adapter: ParkingSessionRepositoryGateway

    @Test
    fun `deve encontrar sessao ativa pela placa`() {
        // Arrange
        val sector = Sector(0, "A", 10.0, 10)
        val spot = ParkingSpot(0, sector, true)
        val vehicle = Vehicle("ACTIVE-001")
        val session = ParkingSession(0, vehicle, spot, Instant.now())

        adapter.save(session) // Salva via adapter para garantir que o mapeamento funcione

        // Act
        val foundSession = adapter.findActiveByPlate("ACTIVE-001")

        // Assert
        assertNotNull(foundSession)
        assertEquals("ACTIVE-001", foundSession?.vehicle?.licensePlate)
        assertNull(foundSession?.exitTime)
    }
}
