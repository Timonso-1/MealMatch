package de.timonso.mealmatch.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "app_user")
class AppUser(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var emailVerified: Boolean = false,

    @Column(nullable = false)
    var displayName: String,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),
)
