package de.timonso.mealmatch.core.storage

import kotlin.concurrent.Volatile

data class Tokens(
    val accessToken: String,
    val refreshToken: String,
)

interface TokenStore {
    fun get(): Tokens?
    fun save(tokens: Tokens)
    fun clear()
}

class InMemoryTokenStore : TokenStore {
    @Volatile
    private var tokens: Tokens? = null

    override fun get(): Tokens? = tokens
    override fun save(tokens: Tokens) { this.tokens = tokens }
    override fun clear() { tokens = null }
}