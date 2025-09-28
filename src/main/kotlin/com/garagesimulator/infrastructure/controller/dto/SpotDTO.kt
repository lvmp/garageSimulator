package com.garagesimulator.infrastructure.controller.dto

data class SpotDTO(
    val id: Long,
    val sector: String,
    val lat: Double,
    val lng: Double,
    val occupied: Boolean
)
