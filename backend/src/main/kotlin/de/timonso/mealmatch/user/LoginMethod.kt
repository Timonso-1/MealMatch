package de.timonso.mealmatch.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "login_method")
class LoginMethod(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", nullable = false, updatable = false)
    val user: AppUser,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val type: LoginType,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),
)