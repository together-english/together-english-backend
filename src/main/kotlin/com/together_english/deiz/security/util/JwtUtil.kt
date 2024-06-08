package com.together_english.deiz.security.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
        @Value("\${jwt.secret}") val secret: String,
        @Value("\${jwt.accessExpiration}") val accessExpiration: Long,
        @Value("\${jwt.refreshExpiration}") val refreshExpiration: Long
) {

    fun generateAccessToken(email: String): String {
        return Jwts.builder()
                .claims().add("email", email).and()
                .issuedAt(Date())
                .expiration(Date(System.currentTimeMillis() + accessExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .compact();

    }

    fun generateRefreshToken(email: String): String {
        return Jwts.builder()
                .claims().add("email", email).and()
                .issuedAt(Date())
                .expiration(Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .compact()
    }

    fun validateTokenExpiration(token: String): Boolean {
        val claimsJws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .build()
                .parseSignedClaims(token)

        return !claimsJws.payload.expiration.before(Date())
    }

}