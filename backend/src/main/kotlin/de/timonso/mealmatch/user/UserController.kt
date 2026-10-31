package de.timonso.mealmatch.user

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val service: UserService) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        val user = service.create(request)
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(user.id)
            .toUri()
        return ResponseEntity.created(location).body(user.toResponse())
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): UserResponse = service.get(id).toResponse()
}

