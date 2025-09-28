package com.garagesimulator.infrastructure.controller.dto

data class SectorDTO(
    val sector: String,
    val base_price: Double,
    val max_capacity: Int,
    val open_hour: String,
    val close_hour: String,
    val duration_limit_minutes: Int
)
