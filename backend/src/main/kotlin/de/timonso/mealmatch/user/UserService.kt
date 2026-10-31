package de.timonso.mealmatch.user

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserService(private val users: AppUserRepository) {

    @Transactional(readOnly = true)
    fun get(id: UUID): AppUser =
        users.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
        }
}
