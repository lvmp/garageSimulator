package com.garagesimulator.domain.model

import java.lang.IllegalArgumentException

/**
 * Representa um veículo no sistema, identificado unicamente por sua placa.
 * Pattern: Value Object, pois seu valor (placa) o define e é imutável.
 */
data class Vehicle(val licensePlate: String) {
    init {
        require(licensePlate.isNotBlank()) { "A placa do veículo não pode ser vazia." }
    }
}
