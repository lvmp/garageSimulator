package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.Sector
import com.garagesimulator.infrastructure.persistence.entity.SectorEntity
import org.springframework.stereotype.Component

@Component
class SectorMapper {
    fun toEntity(domain: Sector) = SectorEntity(
        id = domain.id,
        name = domain.name,
        basePrice = domain.basePrice,
        maxCapacity = domain.maxCapacity
    )
    fun toDomain(entity: SectorEntity) = Sector(
        id = entity.id,
        name = entity.name,
        basePrice = entity.basePrice,
        maxCapacity = entity.maxCapacity
    )
}
