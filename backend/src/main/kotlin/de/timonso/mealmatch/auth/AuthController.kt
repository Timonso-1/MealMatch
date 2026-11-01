package de.timonso.mealmatch.auth

import de.timonso.mealmatch.auth.token.AccessTokenService
import de.timonso.mealmatch.auth.token.RefreshTokenService
import de.timonso.mealmatch.user.UserResponse
import de.timonso.mealmatch.user.toResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val registration: RegistrationService,
    private val loginService: LoginService,
    private val accessTokens: AccessTokenService,
    private val refreshTokens: RefreshTokenService,
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
        val user = registration.register(request)
        val location = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/v1/users/{id}")
            .buildAndExpand(user.id)
            .toUri()
        return ResponseEntity.created(location).body(user.toResponse())
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): LoginResponse {
        val user = loginService.login(request)
        return LoginResponse(
            user = user.toResponse(),
            accessToken = accessTokens.issue(user.id),
            refreshToken = refreshTokens.issue(user.id),
        )
    }
}

