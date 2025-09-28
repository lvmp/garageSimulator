package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.infrastructure.persistence.entity.ParkingSpotEntity
import org.springframework.stereotype.Component

@Component
class ParkingSpotMapper(
    private val sectorMapper: SectorMapper
) {
    fun toEntity(domain: ParkingSpot) = ParkingSpotEntity(
        id = domain.id,
        sector = sectorMapper.toEntity(domain.sector),
        isOccupied = domain.isOccupied,
        latitude = domain.latitude,
        longitude = domain.longitude
    )
    fun toDomain(entity: ParkingSpotEntity) = ParkingSpot(
        id = entity.id,
        sector = sectorMapper.toDomain(entity.sector),
        isOccupied = entity.isOccupied,
        latitude = entity.latitude,
        longitude = entity.longitude
    )
}
