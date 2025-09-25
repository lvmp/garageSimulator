package com.garagesimulator.application.port

import com.garagesimulator.domain.model.ParkingSession
import java.time.LocalDate

/**
 * Port para o repositório de ParkingSession.
 * Define o contrato que a camada de infraestrutura deve implementar para persistir dados de sessão.
 */
interface ParkingSessionRepositoryPort {
    fun save(session: ParkingSession): ParkingSession
    fun findActiveByPlate(licensePlate: String): ParkingSession?
    fun findFinishedByDateAndSector(date: LocalDate, sectorName: String): List<ParkingSession>
}
