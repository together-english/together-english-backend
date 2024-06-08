package com.together_english.deiz.security.util

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JwtUtilTest {

    private val jwtUtil = JwtUtil("testSecrfweewrewrh323r3r238r32r8923r34r23r23eewerfewrfwerwet", 3600000, 86400000) // 테스트용 시크릿과 만료 시간 설정

    @Test
    fun `generateAccessToken should return a valid JWT token`() {
        val email = "test@example.com"
        val token = jwtUtil.generateAccessToken(email)

        assertTrue(token.isNotBlank())
    }

    @Test
    fun `generateRefreshToken should return a valid JWT token`() {
        val email = "test@example.com"
        val token = jwtUtil.generateRefreshToken(email)

        assertTrue(token.isNotBlank())
    }

    @Test
    fun `validateToken should return true for a valid token`() {
        val token = jwtUtil.generateAccessToken("test@example.com")

        assertTrue(jwtUtil.validateTokenExpiration(token))
    }

}