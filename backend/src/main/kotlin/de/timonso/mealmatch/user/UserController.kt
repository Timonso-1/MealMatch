package de.timonso.mealmatch.user

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val service: UserService) {

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal jwt: Jwt): UserResponse =
        service.get(UUID.fromString(jwt.subject)).toResponse()
}
