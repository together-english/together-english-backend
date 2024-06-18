package com.together_english.deiz.security.config

import com.together_english.deiz.security.filter.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
        private val jwtAuthenticaitonFilter: JwtAuthenticationFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/", permitAll)
                authorize("/h2-console/**", permitAll) // H2 콘솔 경로에 대한 접근 허용
            }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            formLogin { }
            httpBasic {disable() }
            headers {
                frameOptions {
                    sameOrigin = true // H2 콘솔이 iframe 내에서 올바르게 로드되도록 설정
                }
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticaitonFilter)
        }
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer() = WebSecurityCustomizer { web ->
        web.ignoring().requestMatchers(AntPathRequestMatcher("/h2-console/**"))
    }


}