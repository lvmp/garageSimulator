package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.infrastructure.persistence.entity.ParkingSessionEntity
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ParkingSessionMapper(
    private val vehicleMapper: VehicleMapper,
    private val parkingSpotMapper: ParkingSpotMapper
) {
    fun toEntity(domain: ParkingSession) = ParkingSessionEntity(
        id = domain.id,
        vehicle = vehicleMapper.toEntity(domain.vehicle),
        parkingSpot = parkingSpotMapper.toEntity(domain.parkingSpot),
        entryTime = domain.entryTime,
        exitTime = domain.exitTime,
        dynamicPricePercentage = domain.dynamicPricePercentage,
        finalCost = domain.finalCost
    )
    fun toDomain(entity: ParkingSessionEntity) = ParkingSession(
        id = entity.id,
        vehicle = vehicleMapper.toDomain(entity.vehicle),
        parkingSpot = parkingSpotMapper.toDomain(entity.parkingSpot),
        entryTime = entity.entryTime,
        exitTime = entity.exitTime,
        dynamicPricePercentage = entity.dynamicPricePercentage,
        finalCost =  entity.finalCost
    )
}
