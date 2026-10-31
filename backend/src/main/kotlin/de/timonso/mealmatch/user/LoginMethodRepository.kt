package de.timonso.mealmatch.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface LoginMethodRepository : JpaRepository<LoginMethod, UUID> {
    fun findAllByUserId(userId: UUID): List<LoginMethod>
    fun existsByUserIdAndType(userId: UUID, type: LoginType): Boolean
}