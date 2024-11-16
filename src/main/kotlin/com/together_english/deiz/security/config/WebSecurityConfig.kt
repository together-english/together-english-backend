package com.together_english.deiz.security.config

import com.together_english.deiz.model.member.Role
import com.together_english.deiz.security.filter.JwtAuthenticationFilter
import com.together_english.deiz.security.util.CustomAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
        private val jwtAuthenticaitonFilter: JwtAuthenticationFilter,
        private var corsConfigurationSource: CorsConfigurationSource
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { corsConfigurationSource = corsConfigurationSource}
            authorizeHttpRequests {
                authorize("/", permitAll)
                authorize("/auth/**", permitAll)
                authorize(HttpMethod.GET, "/circle/**", permitAll)
                authorize("/member/**", hasAuthority(Role.USER.authority))
                authorize(HttpMethod.POST,"/circle/**", hasAuthority(Role.USER.authority))
                authorize(HttpMethod.PUT,"/circle/**", hasAuthority(Role.USER.authority))
                authorize(HttpMethod.POST,"/comment/**", hasAuthority(Role.USER.authority))
                authorize(HttpMethod.PATCH,"/comment/**", hasAuthority(Role.USER.authority))
                authorize("/h2-console/**", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("api-docs/**", permitAll)
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
            exceptionHandling {
                authenticationEntryPoint = CustomAuthenticationEntryPoint()
            }
        }
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer() = WebSecurityCustomizer { web ->
        web.ignoring().requestMatchers("/h2-console/**", "/swagger-ui/**", "/api-docs/**")
    }


}