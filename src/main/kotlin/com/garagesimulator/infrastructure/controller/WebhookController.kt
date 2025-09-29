package com.garagesimulator.infrastructure.controller

import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.exception.NoAvailableSpotException
import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import com.garagesimulator.application.usecase.HandleVehicleEntryUseCase
import com.garagesimulator.application.usecase.HandleVehicleExitUseCase
import com.garagesimulator.infrastructure.controller.dto.WebhookEventDTO
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(WebhookController::class.java)

    @PostMapping
    fun handleWebhookEvent(@Valid @RequestBody event: WebhookEventDTO): ResponseEntity<String> {
        logger.info("Recebendo evento: {}", event.event_type)
        return try {
            when (event.event_type) {
                "ENTRY" -> {
                    if (event.entry_time == null ) {
                        logger.warn("entry_time, é obrigatório para evento ENTRY da placa {}.", event.license_plate)
                        ResponseEntity.badRequest().body("entry_time, lat, e lng são obrigatórios para evento ENTRY.")
                    } else {
                        handleVehicleEntryUseCase.execute(licensePlate =  event.license_plate, entryTime =  event.entry_time, latitude = event.lat, longitude = event.lng)
                        logger.info("Evento ENTRY processado com sucesso para placa {}.", event.license_plate)
                        ResponseEntity.ok("Evento ENTRY processado com sucesso.")
                    }
                }
                "EXIT" -> {
                    event.exit_time?.let { exitTime ->
                        handleVehicleExitUseCase.execute(event.license_plate, exitTime)
                        logger.info("Evento EXIT processado com sucesso para placa {}.", event.license_plate)
                        ResponseEntity.ok("Evento EXIT processado com sucesso.")
                    } ?: run {
                        logger.warn("exit_time é obrigatório para evento EXIT da placa {}.", event.license_plate)
                        ResponseEntity.badRequest().body("exit_time é obrigatório para evento EXIT.")
                    }
                }
                "PARKED" -> {
                    logger.info("Evento PARKED recebido para placa {} (não processado para lógica de negócio).", event.license_plate)
                    ResponseEntity.ok("Evento PARKED recebido (não processado para lógica de negócio).")
                }
                else -> {
                    logger.warn("Tipo de evento desconhecido: {} para placa {}.", event.event_type, event.license_plate)
                    ResponseEntity.badRequest().body("Tipo de evento desconhecido.")
                }
            }
        } catch (e: GarageFullException) {
            logger.warn("Estacionamento lotado para entrada de {}: {}", event.license_plate, e.message)
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
        } catch (e: NoAvailableSpotException) {
            logger.warn("Nenhuma vaga disponível para entrada de {}: {}", event.license_plate, e.message)
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        } catch (e: ParkingSessionNotFoundException) {
            logger.warn("Sessão não encontrada para saída de {}: {}", event.license_plate, e.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            logger.error("Erro interno ao processar evento {} para placa {}: {}", event.event_type, event.license_plate, e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: ${e.message}")
        }
    }
}
