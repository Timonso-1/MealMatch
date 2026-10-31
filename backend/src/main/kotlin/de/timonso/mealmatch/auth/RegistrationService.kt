package de.timonso.mealmatch.auth

import de.timonso.mealmatch.auth.password.PasswordCredential
import de.timonso.mealmatch.auth.password.PasswordCredentialRepository
import de.timonso.mealmatch.user.AppUser
import de.timonso.mealmatch.user.AppUserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class RegistrationService(
    private val users: AppUserRepository,
    private val loginMethods: LoginMethodRepository,
    private val credentials: PasswordCredentialRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun register(request: RegisterRequest): AppUser {
        val email = request.email.trim().lowercase()
        if (users.findByEmailIgnoreCase(email) != null) throw emailTaken()
        try {
            val user = users.saveAndFlush(AppUser(email = email, displayName = request.displayName.trim()))
            val method = loginMethods.saveAndFlush(LoginMethod(user = user, type = LoginType.PASSWORD))
            credentials.save(PasswordCredential(method.id, passwordEncoder.encode(request.password)!!))
            return user
        } catch (e: DataIntegrityViolationException) {
            throw emailTaken()
        }
    }

    private fun emailTaken() =
        ResponseStatusException(HttpStatus.CONFLICT, "Email already registered")
}
