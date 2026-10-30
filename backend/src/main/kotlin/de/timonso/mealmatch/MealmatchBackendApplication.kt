package de.timonso.mealmatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MealmatchBackendApplication

fun main(args: Array<String>) {
	runApplication<MealmatchBackendApplication>(*args)
}
