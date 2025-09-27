package com.garagesimulator.infrastructure.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import com.garagesimulator.application.usecase.HandleVehicleEntryUseCase
import com.garagesimulator.application.usecase.HandleVehicleExitUseCase
import com.garagesimulator.infrastructure.controller.dto.WebhookEventDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDateTime

class WebhookControllerTest {

    private lateinit var mockMvc: MockMvc
    private val handleVehicleEntryUseCase: HandleVehicleEntryUseCase = mockk(relaxed = true)
    private val handleVehicleExitUseCase: HandleVehicleExitUseCase = mockk(relaxed = true)
    private val objectMapper = ObjectMapper().registerModule(kotlinModule()).registerModule(JavaTimeModule())

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(WebhookController(handleVehicleEntryUseCase, handleVehicleExitUseCase)).build()
    }

    @Test
    fun `deve processar evento ENTRY com sucesso`() {
        // Arrange
        val event = WebhookEventDTO(
            license_plate = "ABC-1234",
            entry_time = LocalDateTime.now(),
            event_type = "ENTRY"
        )

        // Act & Assert
        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isOk)

        verify(exactly = 1) { handleVehicleEntryUseCase.execute(event.license_plate, event.entry_time!!) }
    }

    @Test
    fun `deve processar evento EXIT com sucesso`() {
        // Arrange
        val event = WebhookEventDTO(
            license_plate = "ABC-1234",
            exit_time = LocalDateTime.now(),
            event_type = "EXIT"
        )

        // Act & Assert
        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isOk)

        verify(exactly = 1) { handleVehicleExitUseCase.execute(event.license_plate, event.exit_time!!) }
    }

    @Test
    fun `deve retornar 403 FORBIDDEN para GarageFullException`() {
        // Arrange
        val event = WebhookEventDTO(
            license_plate = "ABC-1234",
            entry_time = LocalDateTime.now(),
            event_type = "ENTRY"
        )
        every { handleVehicleEntryUseCase.execute(any(), any()) } throws GarageFullException()

        // Act & Assert
        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isForbidden)
    }

    @Test
    fun `deve retornar 404 NOT FOUND para ParkingSessionNotFoundException`() {
        // Arrange
        val event = WebhookEventDTO(
            license_plate = "ABC-1234",
            exit_time = LocalDateTime.now(),
            event_type = "EXIT"
        )
        every { handleVehicleExitUseCase.execute(any(), any()) } throws ParkingSessionNotFoundException("ABC-1234")

        // Act & Assert
        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isNotFound)
    }
}
