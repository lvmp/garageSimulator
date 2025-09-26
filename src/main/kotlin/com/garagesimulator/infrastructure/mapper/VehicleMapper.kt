package com.garagesimulator.infrastructure.mapper

import com.garagesimulator.domain.model.Vehicle
import com.garagesimulator.infrastructure.persistence.entity.VehicleEntity
import org.springframework.stereotype.Component

@Component
class VehicleMapper {
    fun toEntity(domain: Vehicle) = VehicleEntity(licensePlate = domain.licensePlate)
    fun toDomain(entity: VehicleEntity) = Vehicle(licensePlate = entity.licensePlate)
}
