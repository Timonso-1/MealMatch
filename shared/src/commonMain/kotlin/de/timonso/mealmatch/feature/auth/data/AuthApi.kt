package de.timonso.mealmatch.feature.auth.data

import de.timonso.mealmatch.core.network.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException

class AuthApi(private val client: HttpClient) {

    suspend fun register(email: String, password: String, displayName: String): ApiResult<UserDto> =
        call { client.post("/api/v1/auth/register") { setBody(RegisterRequest(email, password, displayName)) } }

    suspend fun login(email: String, password: String): ApiResult<LoginResponse> =
        call { client.post("/api/v1/auth/login") { setBody(LoginRequest(email, password)) } }

    suspend fun refresh(refreshToken: String): ApiResult<TokenResponse> =
        call { client.post("/api/v1/auth/refresh") { setBody(RefreshRequest(refreshToken)) } }

    suspend fun me(accessToken: String): ApiResult<UserDto> =
        call { client.get("/api/v1/users/me") { bearerAuth(accessToken) } }
}

private suspend inline fun <reified T> call(block: () -> HttpResponse): ApiResult<T> =
    try {
        val response = block()
        if (response.status.isSuccess()) ApiResult.Success(response.body<T>())
        else ApiResult.Failure(response.status.value)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        ApiResult.Failure(null)
    }
