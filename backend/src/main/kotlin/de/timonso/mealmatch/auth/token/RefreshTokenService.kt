package de.timonso.mealmatch.auth.token

import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.UUID
import java.util.HexFormat

@Service
class RefreshTokenService(private val tokens: RefreshTokenRepository) {

    private val random = SecureRandom()

    @Transactional
    fun issue(userId: UUID, familyId: UUID = UUID.randomUUID()): String {
        val raw = ByteArray(32).also(random::nextBytes)
        val token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw)
        tokens.save(
            RefreshToken(
                userId = userId,
                familyId = familyId,
                tokenHash = hash(token),
                expiresAt = Instant.now().plus(30, ChronoUnit.DAYS),
            )
        )
        return token
    }

    private fun hash(token: String): String =
        HexFormat.of().formatHex(
            MessageDigest.getInstance("SHA-256").digest(token.toByteArray())
        )
}