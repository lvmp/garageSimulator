package com.garagesimulator.infrastructure.client

import com.garagesimulator.application.port.SimulatorClientPort
import com.garagesimulator.infrastructure.controller.dto.GarageConfigDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class SimulatorHttpClient(
    private val restClient: RestClient
) : SimulatorClientPort {

    override fun getGarageConfiguration(): GarageConfigDTO {
        val uri = "/garage"
        return restClient.get().uri(uri).retrieve().body(GarageConfigDTO::class.java)
            ?: throw IllegalStateException("Não foi possível obter a configuração da garagem do simulador.")
    }
}
