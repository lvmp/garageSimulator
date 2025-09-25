package com.garagesimulator.application.port

import com.garagesimulator.domain.model.ParkingSpot
import com.garagesimulator.domain.model.Sector

/**
 * Port para o repositório do estado da Garagem.
 * Define o contrato para obter informações sobre vagas e setores.
 */
interface GarageRepositoryPort {
    fun findAvailableSpotInSector(sectorName: String): ParkingSpot?
    fun getOccupiedSpotsCount(): Int
    fun getTotalSpotsCount(): Int
    fun saveAllSpots(spots: List<ParkingSpot>)
    fun saveAllSectors(sectors: List<Sector>)
}
