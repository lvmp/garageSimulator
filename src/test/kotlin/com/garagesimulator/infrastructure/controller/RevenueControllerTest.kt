package com.garagesimulator.infrastructure.controller

import com.garagesimulator.infrastructure.controller.dto.RevenueRequestDTO
import com.garagesimulator.infrastructure.controller.dto.RevenueResponseDTO
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.garagesimulator.application.usecase.GetRevenueUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.math.BigDecimal
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
        val expectedAmount = BigDecimal("123.45")
        every { getRevenueUseCase.execute(any(), any()) } returns expectedAmount

        // Act & Assert
        val result = mockMvc.perform(
            get("/revenue")
                .param("date", request.date.toString())
                .param("sector", request.sector)
        )
            .andExpect(status().isOk)
            .andReturn()

        val responseJson = result.response.contentAsString
        val responseDTO = objectMapper.readValue(responseJson, RevenueResponseDTO::class.java)

        assertEquals(expectedAmount, responseDTO.amount)
        assertEquals("BRL", responseDTO.currency)
    }
}

