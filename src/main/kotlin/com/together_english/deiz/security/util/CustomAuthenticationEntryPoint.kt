package com.together_english.deiz.security.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        response?.contentType = "application/json"
        response?.writer?.write("""{"error": "Unauthorized", "message": "${authException?.message}"}""")
    }
}