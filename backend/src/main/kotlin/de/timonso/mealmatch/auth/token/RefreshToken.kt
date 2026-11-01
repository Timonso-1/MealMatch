package de.timonso.mealmatch.auth.token

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.Column
import java.util.UUID
import java.time.Instant

@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Id val id: UUID = UUID.randomUUID(),
    @Column(name = "app_user_id", nullable = false, updatable = false) val userId: UUID,
    @Column(nullable = false, updatable = false) val familyId: UUID,
    @Column(nullable = false, updatable = false) val tokenHash: String,
    @Column(nullable = false, updatable = false) val expiresAt: Instant,
    var usedAt: Instant? = null,
    var revokedAt: Instant? = null,
    @Column(nullable = false, updatable = false) val createdAt: Instant = Instant.now(),
)