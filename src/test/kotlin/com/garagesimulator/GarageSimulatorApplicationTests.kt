package com.garagesimulator

import com.garagesimulator.application.usecase.LoadInitialGarageConfigurationUseCase
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest
@TestPropertySource(properties = [
	"spring.datasource.url=jdbc:h2:mem:testdb",
	"spring.datasource.driverClassName=org.h2.Driver",
	"spring.datasource.username=sa",
	"spring.datasource.password=",
	"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
])
@MockitoBean(types = [LoadInitialGarageConfigurationUseCase::class])
class GarageSimulatorApplicationTests {

	@Test
	fun contextLoads() {
	}

}
