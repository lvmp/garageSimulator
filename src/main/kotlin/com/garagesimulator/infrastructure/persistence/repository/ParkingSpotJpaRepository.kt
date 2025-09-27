package com.garagesimulator.infrastructure.persistence.repository

import com.garagesimulator.infrastructure.persistence.entity.ParkingSpotEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ParkingSpotJpaRepository : JpaRepository<ParkingSpotEntity, Long> {

    fun findFirstBySectorNameAndIsOccupiedFalse(sectorName: String): ParkingSpotEntity?
}
