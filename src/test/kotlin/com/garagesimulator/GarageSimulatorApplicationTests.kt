package com.garagesimulator

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = [
	"spring.datasource.url=jdbc:h2:mem:testdb",
	"spring.datasource.driverClassName=org.h2.Driver",
	"spring.datasource.username=sa",
	"spring.datasource.password=",
	"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
])
class GarageSimulatorApplicationTests {

	@Test
	fun contextLoads() {
	}

}
