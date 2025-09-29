package com.garagesimulator.infrastructure.controller.exception

import com.garagesimulator.application.exception.GarageFullException
import com.garagesimulator.application.exception.NoAvailableSpotException
import com.garagesimulator.application.exception.ParkingSessionNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(GarageFullException::class)
    fun handleGarageFullException(ex: GarageFullException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(NoAvailableSpotException::class)
    fun handleNoAvailableSpotException(ex: NoAvailableSpotException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(ParkingSessionNotFoundException::class)
    fun handleParkingSessionNotFoundException(ex: ParkingSessionNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.map { it.defaultMessage ?: "Invalid value" }
        return ResponseEntity(ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST)
    }
}

data class ErrorResponse(val message: String?)
data class ValidationErrorResponse(val errors: List<String>)