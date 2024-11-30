package com.together_english.deiz.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

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
                "https://together-english.com"
            )
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}