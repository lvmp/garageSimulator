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

    override fun findAvailableSpot(): ParkingSpot? {
        return spotRepository.findFirstByIsOccupiedFalse()?.let { spotMapper.toDomain(it) }
    }

    override fun getOccupiedSpotsCount(): Int {
        return spotRepository.findAll().count { it.isOccupied }
    }

    override fun getTotalSpotsCount(): Int {
        return spotRepository.count().toInt()
    }

    override fun saveAllSpots(spots: List<ParkingSpot>): List<ParkingSpot> {
        val savedEntities = spotRepository.saveAll(spots.map { spotMapper.toEntity(it) })
        return savedEntities.map { spotMapper.toDomain(it) }
    }

    override fun saveAllSectors(sectors: List<Sector>): List<Sector> {
        val savedEntities = sectorRepository.saveAll(sectors.map { sectorMapper.toEntity(it) })
        return savedEntities.map { sectorMapper.toDomain(it) }
    }

    override fun saveSpot(spot: ParkingSpot): ParkingSpot {
        val savedEntity = spotRepository.save(spotMapper.toEntity(spot))
        return spotMapper.toDomain(savedEntity)
    }

    override fun findSpotByCoordinates(latitude: Double, longitude: Double): ParkingSpot? {
        return spotRepository.findByLatitudeAndLongitude(latitude, longitude)?.let { spotMapper.toDomain(it) }
    }

    override fun findAllSectors(): List<Sector> {
        return sectorRepository.findAll().toList().map { sectorMapper.toDomain(it) }
    }
}
