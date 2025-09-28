package com.garagesimulator.application.port

import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector

/**
 * Port para o repositório do estado da Garagem.
 * Define o contrato para obter informações sobre vagas e setores.
 */
interface GarageRepositoryPort {
    fun findAvailableSpot(): ParkingSpot?
    fun getOccupiedSpotsCount(): Int
    fun getTotalSpotsCount(): Int
    fun saveAllSpots(spots: List<ParkingSpot>): List<ParkingSpot>
    fun saveSpot(spot: ParkingSpot): ParkingSpot
    fun findSpotByCoordinates(latitude: Double, longitude: Double): ParkingSpot?
    fun findAllSectors(): List<Sector>
    fun saveAllSectors(sectors: List<Sector>): List<Sector>
}
