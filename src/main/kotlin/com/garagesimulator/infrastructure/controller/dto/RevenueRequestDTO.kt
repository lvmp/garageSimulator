package com.garagesimulator.infrastructure.controller.dto

import java.time.LocalDate

data class RevenueRequestDTO(
    val date: LocalDate,
    val sector: String
)
