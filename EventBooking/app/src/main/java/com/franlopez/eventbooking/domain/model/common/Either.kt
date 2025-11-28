package com.franlopez.eventbooking.domain.model.common

sealed class Either<out SUCCESS, out ERROR> {
    data class Success<out SUCCESS>(val data: SUCCESS) : Either<SUCCESS, Nothing>()
    data class Failure<out ERROR>(val error: ERROR): Either<Nothing, ERROR>()
}

fun <SUCCESS> eitherSuccess(data: SUCCESS) = Either.Success(data)
fun <ERROR> eitherFailure(error: ERROR) = Either.Failure(error)