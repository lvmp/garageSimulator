package com.garagesimulator.infrastructure.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import com.garagesimulator.application.usecase.HandleVehicleEntryUseCase
import com.garagesimulator.application.usecase.HandleVehicleExitUseCase
import com.garagesimulator.infrastructure.controller.dto.WebhookEventDTO
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(WebhookController::class)
class WebhookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var handleVehicleEntryUseCase: HandleVehicleEntryUseCase

    @MockBean
    private lateinit var handleVehicleExitUseCase: HandleVehicleExitUseCase

    @Test
    fun `deve processar evento ENTRY com sucesso`() {
        val event = WebhookEventDTO(
            license_plate = "ABC1D23",
            entry_time = LocalDateTime.now(),
            lat = -23.0,
            lng = -46.0,
            event_type = "ENTRY"
        )

        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isOk)

        verify(handleVehicleEntryUseCase).execute(event.license_plate, event.entry_time!!, event.lat, event.lng)
    }

    @Test
    fun `deve processar evento EXIT com sucesso`() {
        val event = WebhookEventDTO(
            license_plate = "ABC-1234",
            exit_time = LocalDateTime.now(),
            event_type = "EXIT"
        )

        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isOk)

        verify(handleVehicleExitUseCase).execute(event.license_plate, event.exit_time!!)
    }

    @Test
    fun `deve retornar 403 FORBIDDEN para GarageFullException`() {
        val event = WebhookEventDTO(
            license_plate = "ABC-1234",
            entry_time = LocalDateTime.now(),
            lat = -23.0,
            lng = -46.0,
            event_type = "ENTRY"
        )
        `when`(handleVehicleEntryUseCase.execute(any(), any(), any(), any()))
            .thenThrow(GarageFullException())

        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isForbidden)
    }

    @Test
    fun `deve retornar 404 NOT FOUND para ParkingSessionNotFoundException`() {
        val event = WebhookEventDTO(
            license_plate = "ABC-1234",
            exit_time = LocalDateTime.now(),
            event_type = "EXIT"
        )
        `when`(handleVehicleExitUseCase.execute(any(), any()))
            .thenThrow(ParkingSessionNotFoundException("ABC-1234"))

        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deve retornar 400 BAD REQUEST para placa invalida`() {
        val event = WebhookEventDTO(
            license_plate = "ABC-123",
            entry_time = LocalDateTime.now(),
            lat = -23.0,
            lng = -46.0,
            event_type = "ENTRY"
        )

        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0]").value("Field 'license_plate' is not in a valid format."))
    }

    @Test
    fun `deve retornar 400 BAD REQUEST para placa com formato incorreto`() {
        val event = WebhookEventDTO(
            license_plate = "123-ABCD",
            entry_time = LocalDateTime.now(),
            lat = -23.0,
            lng = -46.0,
            event_type = "ENTRY"
        )

        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0]").value("Field 'license_plate' is not in a valid format."))
    }

    @Test
    fun `deve processar com sucesso placa no formato Mercosul sem hifen`() {
        val event = WebhookEventDTO(
            license_plate = "ABC1D23",
            entry_time = LocalDateTime.now(),
            lat = -23.0,
            lng = -46.0,
            event_type = "ENTRY"
        )

        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isOk)
    }

    @Test
    fun `deve processar com sucesso placa no formato antigo com hifen`() {
        val event = WebhookEventDTO(
            license_plate = "ABC-1234",
            entry_time = LocalDateTime.now(),
            lat = -23.0,
            lng = -46.0,
            event_type = "ENTRY"
        )

        mockMvc.perform(post("/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isOk)
    }

    // Helper for Mockito `when` with `any()`
    private fun <T> any(): T = org.mockito.ArgumentMatchers.any()
}
