package com.garagesimulator.infrastructure.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "vehicles")
data class VehicleEntity(
    @Id
    val licensePlate: String,
)
