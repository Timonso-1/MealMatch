package de.timonso.mealmatch.core.network

sealed interface ApiResult<out T> {
    data class Success<T>(val value: T) : ApiResult<T>
    data class Failure(val status: Int?) : ApiResult<Nothing>
}