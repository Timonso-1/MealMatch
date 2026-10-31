package de.timonso.mealmatch.auth.password

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PasswordCredentialRepository : JpaRepository<PasswordCredential, UUID>
