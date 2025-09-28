package com.garagesimulator.infrastructure.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "parking_sessions")
data class ParkingSessionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(cascade = [CascadeType.ALL])
    val vehicle: VehicleEntity,

    @ManyToOne(cascade = [CascadeType.MERGE])
    val parkingSpot: ParkingSpotEntity,

    val entryTime: LocalDateTime,
    var exitTime: LocalDateTime? = null,
    val dynamicPricePercentage: BigDecimal? = BigDecimal.ZERO,
    var finalCost: BigDecimal? = BigDecimal.ZERO,
)