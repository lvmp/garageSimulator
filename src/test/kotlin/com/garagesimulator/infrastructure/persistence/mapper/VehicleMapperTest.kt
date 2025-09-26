package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.Vehicle
import com.garagesimulator.infrastructure.persistence.entity.VehicleEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VehicleMapperTest {

    private val mapper = VehicleMapper()

    @Test
    fun `deve mapear Vehicle de dominio para entidade`() {
        val domain = Vehicle("ABC-123")
        val entity = mapper.toEntity(domain)
        assertEquals(domain.licensePlate, entity.licensePlate)
    }

    @Test
    fun `deve mapear Vehicle de entidade para dominio`() {
        val entity = VehicleEntity("ABC-123")
        val domain = mapper.toDomain(entity)
        assertEquals(entity.licensePlate, domain.licensePlate)
    }
}
