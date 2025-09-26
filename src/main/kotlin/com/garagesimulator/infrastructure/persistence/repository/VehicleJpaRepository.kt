package com.garagesimulator.infrastructure.persistence.repository

import com.garagesimulator.infrastructure.persistence.entity.VehicleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VehicleJpaRepository : JpaRepository<VehicleEntity, String>
