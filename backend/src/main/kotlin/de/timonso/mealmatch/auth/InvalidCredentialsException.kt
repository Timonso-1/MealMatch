package de.timonso.mealmatch.auth

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InvalidCredentialsException :
        ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password")