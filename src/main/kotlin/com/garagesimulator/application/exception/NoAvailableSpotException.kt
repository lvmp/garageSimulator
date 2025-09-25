package com.garagesimulator.application.exception

class NoAvailableSpotException(sector: String) : RuntimeException("Não há vagas disponíveis no setor $sector.")
