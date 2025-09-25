package com.garagesimulator.application.exception

class ParkingSessionNotFoundException(licensePlate: String) : RuntimeException("Não foi encontrada uma sessão ativa para a placa $licensePlate.")
