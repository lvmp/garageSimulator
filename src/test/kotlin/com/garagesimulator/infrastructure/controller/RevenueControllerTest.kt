package com.garagesimulator.infrastructure.controller

import com.garagesimulator.application.usecase.GetRevenueUseCase
import com.garagesimulator.infrastructure.controller.dto.RevenueRequestDTO
import com.garagesimulator.infrastructure.controller.dto.RevenueResponseDTO
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.Instant
import java.time.LocalDate

class RevenueControllerTest {

    private lateinit var mockMvc: MockMvc
    private val getRevenueUseCase: GetRevenueUseCase = mockk(relaxed = true)
    private val objectMapper = ObjectMapper().registerModule(kotlinModule()).registerModule(JavaTimeModule())

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(RevenueController(getRevenueUseCase)).build()
    }

    @Test
    fun `deve retornar receita com sucesso`() {
        // Arrange
        val request = RevenueRequestDTO(date = LocalDate.now(), sector = "A")
        val expectedAmount = 123.45
        every { getRevenueUseCase.execute(any(), any()) } returns expectedAmount

        // Act & Assert
        mockMvc.perform(get("/revenue")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.amount").value(expectedAmount))
            .andExpect(jsonPath("$.currency").value("BRL"))
    }

    @Test
    fun `deve retornar 400 Bad Request para data invalida`() {
        // Arrange
        val invalidRequestContent = "{\"date\":\"invalid-date\", \"sector\":\"A\"}"

        // Act & Assert
        mockMvc.perform(get("/revenue")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidRequestContent))
            .andExpect(status().isBadRequest)
    }
}
