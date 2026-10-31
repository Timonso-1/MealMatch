package de.timonso.mealmatch.user

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserService(private val users: AppUserRepository) {

    @Transactional
    fun create(request: CreateUserRequest): AppUser {
        val email = request.email.trim().lowercase()
        if (users.findByEmailIgnoreCase(email) != null) throw emailTaken()
        try {
            return users.saveAndFlush(
                AppUser(
                    email = email,
                    displayName = request.displayName
                ))
        } catch (e: DataIntegrityViolationException) {
            throw emailTaken()
        }
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): AppUser =
        users.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
        }

    private fun emailTaken() =
        ResponseStatusException(HttpStatus.CONFLICT, "Email already registered")
}
