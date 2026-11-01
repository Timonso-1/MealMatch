package de.timonso.mealmatch.auth

import de.timonso.mealmatch.auth.password.PasswordCredentialRepository
import de.timonso.mealmatch.user.AppUser
import de.timonso.mealmatch.user.AppUserRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val users: AppUserRepository,
    private val loginMethods: LoginMethodRepository,
    private val credentials: PasswordCredentialRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    private val dummyHash: String = passwordEncoder.encode("dummy-password")!!

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): AppUser {
        val user = users.findByEmailIgnoreCase(request.email.trim().lowercase())
        val hash = user
            ?.let { loginMethods.findByUserIdAndType(it.id, LoginType.PASSWORD) }
            ?.let { credentials.findById(it.id).orElse(null) }
            ?.passwordHash
        val valid = passwordEncoder.matches(request.password, hash ?: dummyHash)
        if (user == null || hash == null || !valid) throw InvalidCredentialsException()
        return user
    }
}