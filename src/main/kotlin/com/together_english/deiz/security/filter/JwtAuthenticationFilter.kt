package com.together_english.deiz.security.filter

import com.together_english.deiz.security.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
        private val jwtUtil: JwtUtil,
        private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private val LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization")
        val cookies = request.cookies

        if(cookies.isNotEmpty()) {
            for(cookie in cookies) {
                if(cookie.name.equals("refresh_token")) {
                    var refresh_token = cookie.value
                    println(refresh_token)
                }
            }
        }

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            try {
                val token = authHeader.substring(7)
                if (jwtUtil.validateToken(token)) {
                    val username = jwtUtil.verifyAndextractUsername(token)
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    val authentication
                    = UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: Exception) {
                LOGGER.error("Failed to authenticate user", e)
            }
        }

        filterChain.doFilter(request, response)
    }
}