package com.garagesimulator.infrastructure.controller

import com.garagesimulator.application.usecase.GetRevenueUseCase
import com.garagesimulator.infrastructure.controller.dto.RevenueRequestDTO
import com.garagesimulator.infrastructure.controller.dto.RevenueResponseDTO
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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

    private val logger = LoggerFactory.getLogger(RevenueController::class.java)

    @GetMapping
    fun getRevenue(@RequestParam date: LocalDate, @RequestParam sector: String): ResponseEntity<RevenueResponseDTO> {
        logger.info("Recebendo requisição de receita para data {} e setor {}", date, sector)
        val amount = getRevenueUseCase.execute(date = date, sectorName = sector)
        val response = RevenueResponseDTO(amount = amount, timestamp = Instant.now())
        logger.info("Receita calculada para data {} e setor {}: {}", date, sector, amount)
        return ResponseEntity.ok(response)
    }
}
