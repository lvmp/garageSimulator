package com.garagesimulator.infrastructure.persistence.repository

import com.garagesimulator.infrastructure.persistence.entity.ParkingSessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ParkingSessionJpaRepository : JpaRepository<ParkingSessionEntity, Long> {

    @Query("SELECT p FROM ParkingSessionEntity p WHERE p.vehicle.licensePlate = :licensePlate AND p.exitTime IS NULL")
    fun findActiveByPlate(licensePlate: String): ParkingSessionEntity?

    @Query("SELECT p FROM ParkingSessionEntity p WHERE FUNCTION('DATE', p.exitTime) = :date AND p.parkingSpot.sector.name = :sectorName")
    fun findFinishedByDateAndSector(date: Instant, sectorName: String): List<ParkingSessionEntity>
}
