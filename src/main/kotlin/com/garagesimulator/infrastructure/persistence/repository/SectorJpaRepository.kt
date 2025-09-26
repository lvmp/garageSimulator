package com.garagesimulator.infrastructure.persistence.repository

import com.garagesimulator.infrastructure.persistence.entity.SectorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SectorJpaRepository : JpaRepository<SectorEntity, Long>
