package com.garagesimulator.infrastructure.controller.dto

data class GarageConfigDTO(
    val garage: List<SectorDTO>,
    val spots: List<SpotDTO>
)
