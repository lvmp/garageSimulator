package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.Sector
import com.garagesimulator.infrastructure.persistence.entity.SectorEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalTime

class SectorMapperTest {

    private val mapper = SectorMapper()

    @Test
    fun `deve mapear Sector de dominio para entidade`() {
        val domain = Sector(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val entity = mapper.toEntity(domain)
        assertEquals(domain.id, entity.id)
        assertEquals(domain.name, entity.name)
        assertEquals(domain.basePrice, entity.basePrice)
        assertEquals(domain.maxCapacity, entity.maxCapacity)
        assertEquals(domain.openHour, entity.openHour)
        assertEquals(domain.closeHour, entity.closeHour)
        assertEquals(domain.durationLimitMinutes, entity.durationLimitMinutes)
    }

    @Test
    fun `deve mapear Sector de entidade para dominio`() {
        val entity = SectorEntity(1L, "A", BigDecimal("10.0"), 100, LocalTime.MIN, LocalTime.MAX, 1440)
        val domain = mapper.toDomain(entity)
        assertEquals(entity.id, domain.id)
        assertEquals(entity.name, domain.name)
        assertEquals(entity.basePrice, domain.basePrice)
        assertEquals(entity.maxCapacity, domain.maxCapacity)
        assertEquals(entity.openHour, domain.openHour)
        assertEquals(entity.closeHour, domain.closeHour)
        assertEquals(entity.durationLimitMinutes, entity.durationLimitMinutes)
    }
}
