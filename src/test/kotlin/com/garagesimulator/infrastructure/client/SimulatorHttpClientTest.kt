package com.garagesimulator.infrastructure.client

import com.garagesimulator.application.port.SimulatorClientPort
import com.garagesimulator.infrastructure.controller.dto.GarageConfigDTO
import com.garagesimulator.infrastructure.controller.dto.SectorDTO
import com.garagesimulator.infrastructure.controller.dto.SpotDTO
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient

class SimulatorHttpClientTest {

    private lateinit var restClient: RestClient
    private lateinit var simulatorClient: SimulatorClientPort

    @BeforeEach
    fun setUp() {
        restClient = mockk()
        simulatorClient = SimulatorHttpClient(restClient)
    }

    @Test
    fun `deve obter configuracao da garagem com sucesso`() {
        // Arrange
        val expectedGarageConfig = GarageConfigDTO(
            garage = listOf(SectorDTO("A", 10.0, 100)),
            spots = listOf(SpotDTO(1L, "A", -23.0, -46.0))
        )

        every { restClient.get().uri(any<String>()).retrieve().body(GarageConfigDTO::class.java) } returns expectedGarageConfig

        // Act
        val actualGarageConfig = simulatorClient.getGarageConfiguration()

        // Assert
        assertEquals(expectedGarageConfig, actualGarageConfig)
    }
}
