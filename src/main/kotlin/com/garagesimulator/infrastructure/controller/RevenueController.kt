package com.garagesimulator.infrastructure.controller

import com.garagesimulator.application.usecase.GetRevenueUseCase
import com.garagesimulator.infrastructure.controller.dto.RevenueRequestDTO
import com.garagesimulator.infrastructure.controller.dto.RevenueResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.LocalDate

@RestController
@RequestMapping("/revenue")
class RevenueController(
    private val getRevenueUseCase: GetRevenueUseCase
) {

    @GetMapping
    fun getRevenue(@RequestParam date: LocalDate, @RequestParam sector: String): ResponseEntity<RevenueResponseDTO> {
        val amount = getRevenueUseCase.execute(date = date, sectorName = sector)
        val response = RevenueResponseDTO(amount = amount, timestamp = Instant.now())
        return ResponseEntity.ok(response)
    }
}
