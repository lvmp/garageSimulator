package com.garagesimulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GarageSimulatorApplication

fun main(args: Array<String>) {
	runApplication<GarageSimulatorApplication>(*args)
}
