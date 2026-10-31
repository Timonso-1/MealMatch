package de.timonso.mealmatch.user

import de.timonso.mealmatch.auth.LoginMethod
import de.timonso.mealmatch.auth.LoginMethodRepository
import de.timonso.mealmatch.auth.LoginType
import de.timonso.mealmatch.auth.password.PasswordCredential
import de.timonso.mealmatch.auth.password.PasswordCredentialRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserService(
    private val users: AppUserRepository,
    private val loginMethods: LoginMethodRepository,
    private val credential: PasswordCredentialRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun create(request: CreateUserRequest): AppUser {
        val email = request.email.trim().lowercase()
        if (users.findByEmailIgnoreCase(email) != null) throw emailTaken()
        try {
            val user = users.saveAndFlush(AppUser(email = email, displayName = request.displayName.trim()))
            val method = loginMethods.saveAndFlush(LoginMethod(user = user, type = LoginType.PASSWORD))
            credential.save(PasswordCredential(method.id, passwordEncoder.encode(request.password)!!))
            return user
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
