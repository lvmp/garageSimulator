package com.garagesimulator.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "parking_sessions")
data class ParkingSessionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(cascade = [jakarta.persistence.CascadeType.ALL])
    val vehicle: VehicleEntity,

    @ManyToOne(cascade = [jakarta.persistence.CascadeType.ALL])
    val parkingSpot: ParkingSpotEntity,

    val entryTime: LocalDateTime,
    var exitTime: LocalDateTime? = null,
    val dynamicPricePercentage: Double = 0.0,
    var finalCost: Double? = null,
)