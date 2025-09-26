package com.garagesimulator.infrastructure.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

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

    val entryTime: Instant,
    var exitTime: Instant? = null,
    val dynamicPricePercentage: Double = 0.0,
    var finalCost: Double? = null,
)