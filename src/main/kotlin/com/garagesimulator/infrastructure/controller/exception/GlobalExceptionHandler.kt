package com.garagesimulator.infrastructure.controller.exception

import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.exception.NoAvailableSpotException
import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(GarageFullException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleGarageFullException(ex: GarageFullException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoAvailableSpotException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNoAvailableSpotException(ex: NoAvailableSpotException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ParkingSessionNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleParkingSessionNotFoundException(ex: ParkingSessionNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.NOT_FOUND)
    }
}

data class ErrorResponse(val message: String?)
