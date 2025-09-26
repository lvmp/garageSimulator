package com.garagesimulator.infrastructure.controller

import com.garagesimulator.application.usecase.GetRevenueUseCase
import com.garagesimulator.infrastructure.controller.dto.RevenueRequestDTO
import com.garagesimulator.infrastructure.controller.dto.RevenueResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/revenue")
class RevenueController(
    private val getRevenueUseCase: GetRevenueUseCase
) {

    @GetMapping
    fun getRevenue(@RequestBody request: RevenueRequestDTO): ResponseEntity<RevenueResponseDTO> {
        val amount = getRevenueUseCase.execute(request.date, request.sector)
        val response = RevenueResponseDTO(amount = amount, timestamp = Instant.now())
        return ResponseEntity.ok(response)
    }
}
