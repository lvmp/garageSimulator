package com.garagesimulator.infrastructure.gateway

import com.garagesimulator.infrastructure.persistence.repository.ParkingSessionJpaRepository
import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import com.garagesimulator.domain.model.ParkingSession
import com.garagesimulator.infrastructure.mapper.ParkingSessionMapper
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ParkingSessionRepositoryGateway(
    private val repository: ParkingSessionJpaRepository,
    private val mapper: ParkingSessionMapper
) : ParkingSessionRepositoryPort {

    override fun save(session: ParkingSession): ParkingSession {
        val entity = mapper.toEntity(session)
        return mapper.toDomain(repository.save(entity))
    }

    override fun findActiveByPlate(licensePlate: String): ParkingSession? {
        return repository.findActiveByPlate(licensePlate)?.let { mapper.toDomain(it) }
    }

    override fun findFinishedByDateAndSector(date: LocalDate, sectorName: String): List<ParkingSession> {
        // Nota: A conversão de LocalDate para Instant pode precisar de ajustes dependendo do fuso horário do DB.
        // Para simplificar, usamos o início do dia.
        val startOfDay = date.atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
        return repository.findFinishedByDateAndSector(startOfDay, sectorName).map { mapper.toDomain(it) }
    }
}
