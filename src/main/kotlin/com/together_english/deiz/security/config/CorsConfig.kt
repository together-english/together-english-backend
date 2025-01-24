package com.together_english.deiz.security.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig(
    @Value("\${domain.url}") val devDomainUrl: String,
) {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf(
                "http://localhost:3000",
                "http://localhost:8080",
                "http://localhost:8080/swagger-ui.html",
                "http://together-english-load-balancer1-1502615571.ap-northeast-2.elb.amazonaws.com",
                "http://ec2-54-180-251-225.ap-northeast-2.compute.amazonaws.com",
                "http://api.together-english.com",
                "https://api.together-english.com",
                "http://together-english.com",
                "https://together-english.com",
                "http://www.together-english.com",
                "https://www.together-english.com",
                "http://localhost:80",
                "http://localhost:80/swagger-ui.html",
                "http://localhost:4040",
                devDomainUrl,
                "${devDomainUrl}/swagger-ui.html"
            )
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            exposedHeaders = listOf("Authorization")
            allowCredentials = true
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}