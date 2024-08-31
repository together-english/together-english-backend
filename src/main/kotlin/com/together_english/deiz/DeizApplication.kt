package com.together_english.deiz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class DeizApplication

fun main(args: Array<String>) {
	runApplication<DeizApplication>(*args)
}
