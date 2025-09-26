package com.garagesimulator.infrastructure.gateway

import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector
import com.garagesimulator.infrastructure.persistence.repository.ParkingSpotJpaRepository
import com.garagesimulator.infrastructure.persistence.repository.SectorJpaRepository
import com.garagesimulator.infrastructure.mapper.ParkingSpotMapper
import com.garagesimulator.infrastructure.mapper.SectorMapper
import org.springframework.stereotype.Component

@Component
class GarageRepositoryGateway(
    private val spotRepository: ParkingSpotJpaRepository,
    private val sectorRepository: SectorJpaRepository,
    private val spotMapper: ParkingSpotMapper,
    private val sectorMapper: SectorMapper
) : GarageRepositoryPort {

    override fun findAvailableSpotInSector(sectorName: String): ParkingSpot? {
        return spotRepository.findFirstAvailableInSector(sectorName)?.let { spotMapper.toDomain(it) }
    }

    override fun getOccupiedSpotsCount(): Int {
        return spotRepository.findAll().count { it.isOccupied }
    }

    override fun getTotalSpotsCount(): Int {
        return spotRepository.count().toInt()
    }

    override fun saveAllSpots(spots: List<ParkingSpot>) {
        spotRepository.saveAll(spots.map { spotMapper.toEntity(it) })
    }

    override fun saveAllSectors(sectors: List<Sector>) {
        sectorRepository.saveAll(sectors.map { sectorMapper.toEntity(it) })
    }
}
