package com.garagesimulator.infrastructure.persistence.repository

import com.garagesimulator.infrastructure.persistence.entity.ParkingSpotEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParkingSpotJpaRepository : JpaRepository<ParkingSpotEntity, Long> {

    fun findFirstByIsOccupiedFalse(): ParkingSpotEntity?
    fun findByLatitudeAndLongitude(latitude: Double, longitude: Double): ParkingSpotEntity?
}
