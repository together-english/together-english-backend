package com.together_english.deiz.data.member

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ADMIN() {
        override fun getAuthority(): String {
            return "ROLE_ADMIN"
        }
    },
    USER {
        override fun getAuthority(): String {
            return "ROLE_USER"
        }
    }
}