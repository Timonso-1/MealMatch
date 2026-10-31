package de.timonso.mealmatch.auth.password

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "password_credential")
class PasswordCredential(
    @Id
    @Column(name = "login_method_id")
    val loginMethodId: UUID,

    @Column(nullable = false)
    val passwordHash: String,

    @Column(nullable = false)
    var changedAt: Instant = Instant.now(),
)

