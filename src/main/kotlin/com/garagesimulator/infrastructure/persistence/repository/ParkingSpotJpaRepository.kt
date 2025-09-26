package com.garagesimulator.infrastructure.persistence.repository

import com.garagesimulator.infrastructure.persistence.entity.ParkingSpotEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ParkingSpotJpaRepository : JpaRepository<ParkingSpotEntity, Long> {

    @Query("SELECT s FROM ParkingSpotEntity s WHERE s.sector.name = :sectorName AND s.isOccupied = false")
    fun findFirstAvailableInSector(sectorName: String): ParkingSpotEntity?
}
