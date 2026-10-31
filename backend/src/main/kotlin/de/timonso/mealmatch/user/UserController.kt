package de.timonso.mealmatch.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val service: UserService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): UserResponse = service.get(id).toResponse()
}
