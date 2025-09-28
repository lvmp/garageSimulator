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
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(23, 59, 59, 999999999)
        return repository.findFinishedByDateAndSector(startOfDay, endOfDay, sectorName).map { mapper.toDomain(it) }
    }
}
