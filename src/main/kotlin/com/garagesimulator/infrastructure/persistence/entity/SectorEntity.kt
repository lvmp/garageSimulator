package com.garagesimulator.infrastructure.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

import java.math.BigDecimal
import java.time.LocalTime

@Entity
@Table(name = "sectors")
data class SectorEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val basePrice: BigDecimal,
    val maxCapacity: Int,
    val openHour: LocalTime,
    val closeHour: LocalTime,
    val durationLimitMinutes: Int,
)
