package com.garagesimulator.infrastructure.controller

import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.exception.NoAvailableSpotException
import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import com.garagesimulator.application.usecase.HandleVehicleEntryUseCase
import com.garagesimulator.application.usecase.HandleVehicleExitUseCase
import com.garagesimulator.infrastructure.controller.dto.WebhookEventDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/webhook")
class WebhookController(
    private val handleVehicleEntryUseCase: HandleVehicleEntryUseCase,
    private val handleVehicleExitUseCase: HandleVehicleExitUseCase
) {

    @PostMapping
    fun handleWebhookEvent(@RequestBody event: WebhookEventDTO): ResponseEntity<String> {
        return try {
            when (event.event_type) {
                "ENTRY" -> {
                    event.entry_time?.let { entryTime ->
                        handleVehicleEntryUseCase.execute(event.license_plate, entryTime)
                        ResponseEntity.ok("Evento ENTRY processado com sucesso.")
                    } ?: ResponseEntity.badRequest().body("entry_time é obrigatório para evento ENTRY.")
                }
                "EXIT" -> {
                    event.exit_time?.let { exitTime ->
                        handleVehicleExitUseCase.execute(event.license_plate, exitTime)
                        ResponseEntity.ok("Evento EXIT processado com sucesso.")
                    } ?: ResponseEntity.badRequest().body("exit_time é obrigatório para evento EXIT.")
                }
                "PARKED" -> {
                    // Lógica para PARKED (se necessário, atualmente não impacta a regra de negócio principal)
                    ResponseEntity.ok("Evento PARKED recebido (não processado para lógica de negócio).")
                }
                else -> ResponseEntity.badRequest().body("Tipo de evento desconhecido.")
            }
        } catch (e: GarageFullException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
        } catch (e: NoAvailableSpotException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        } catch (e: ParkingSessionNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: ${e.message}")
        }
    }
}
