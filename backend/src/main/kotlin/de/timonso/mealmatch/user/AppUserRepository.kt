package de.timonso.mealmatch.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AppUserRepository : JpaRepository<AppUser, UUID> {
    fun findByEmailIgnoreCase(email: String): AppUser?
}