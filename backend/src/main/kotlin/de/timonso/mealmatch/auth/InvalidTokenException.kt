package de.timonso.mealmatch.auth

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InvalidTokenException :
    ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token")
