package com.garagesimulator.application.port

import com.garagesimulator.infrastructure.controller.dto.GarageConfigDTO

interface SimulatorClientPort {
    fun getGarageConfiguration(): GarageConfigDTO
}
