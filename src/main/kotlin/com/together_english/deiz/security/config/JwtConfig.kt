package com.together_english.deiz.security.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
        @Value("\${jwt.secret}") val secret: String,
        @Value("\${jwt.accessExpiration}") val accessExpiration: Long,
        @Value("\${jwt.refreshExpiration}") val refreshExpiration: Long
)