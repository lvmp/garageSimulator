package com.garagesimulator.infrastructure.configuration

import com.garagesimulator.application.port.GarageRepositoryPort
import com.garagesimulator.application.port.ParkingSessionRepositoryPort
import com.garagesimulator.application.usecase.GetRevenueUseCase
import com.garagesimulator.application.usecase.HandleVehicleEntryUseCase
import com.garagesimulator.application.usecase.HandleVehicleExitUseCase
import com.garagesimulator.application.usecase.LoadInitialGarageConfigurationUseCase
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class ApplicationConfig(
    private val loadInitialGarageConfigurationUseCase: LoadInitialGarageConfigurationUseCase
) {

    @Bean
    fun handleVehicleEntryUseCase(
        garageRepository: GarageRepositoryPort,
        parkingSessionRepository: ParkingSessionRepositoryPort,
    ): HandleVehicleEntryUseCase {
        return HandleVehicleEntryUseCase(garageRepository, parkingSessionRepository)
    }

    @Bean
    fun handleVehicleExitUseCase(
        parkingSessionRepository: ParkingSessionRepositoryPort,
        garageRepository: GarageRepositoryPort
    ): HandleVehicleExitUseCase {
        return HandleVehicleExitUseCase(parkingSessionRepository, garageRepository)
    }

    @Bean
    fun getRevenueUseCase(
        parkingSessionRepository: ParkingSessionRepositoryPort
    ): GetRevenueUseCase {
        return GetRevenueUseCase(parkingSessionRepository)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun runInitialConfiguration() {
        loadInitialGarageConfigurationUseCase.execute()
    }
}
