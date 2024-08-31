package com.together_english.deiz.security.util

import com.together_english.deiz.data.JwtToken
import com.together_english.deiz.repository.MemberRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
        @Value("\${jwt.secret}") private val secret: String,
        @Value("\${jwt.accessExpiration}") val accessExpiration: Long,
        @Value("\${jwt.refreshExpiration}") val refreshExpiration: Long,
        private val memberRepository: MemberRepository
) {

    private val LOGGER = LoggerFactory.getLogger(JwtUtil::class.java)

    fun generateAllToken(email: String): JwtToken {
        return JwtToken(
                accessToken = generateAccessToken(email),
                refreshToken = generateRefreshToken(email)
        )
    }

    fun generateAccessToken(email: String): String {
        LOGGER.info("access token 생성 시작")
        return Jwts.builder()
                .header().add("typ", "access").and()
                .claims().add("email", email).and()
                .issuedAt(Date())
                .expiration(Date(System.currentTimeMillis() + accessExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .compact()

    }

    fun generateRefreshToken(email: String): String {
        LOGGER.info("refresh token 생성 시작")
        return Jwts.builder()
                .header().add("typ", "refresh").and()
                .claims().add("email", email).and()
                .issuedAt(Date())
                .expiration(Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .compact()
    }

    fun validateToken(token: String): Boolean {
        LOGGER.info("토큰 검증 시작")
        val claimsJws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .build()
                .parseSignedClaims(token)

        val email = claimsJws.payload["email"] as String

        memberRepository.findByEmail(email).orElseThrow {
            UsernameNotFoundException(email+"해당 유저가 존재하지 않습니다.")
        }

        return !claimsJws.payload.expiration.before(Date())
    }

    fun verifyAndextractUsername(token: String): String {
        LOGGER.info("get authentication 시작")
        val claimsJws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .build()
                .parseSignedClaims(token)

        return claimsJws.payload["email"] as String
    }

}