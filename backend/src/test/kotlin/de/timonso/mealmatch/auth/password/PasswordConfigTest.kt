package de.timonso.mealmatch.auth.password


import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PasswordConfigTest {
    private val encoder = PasswordConfig().passwordEncoder()

    @Test
    fun `hash starts with argon2id marker`() {
    val hash = encoder.encode("geheim123")
    assertTrue(hash!!.startsWith("\$argon2id\$"))
    }
}