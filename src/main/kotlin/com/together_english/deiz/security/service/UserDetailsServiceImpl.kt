package com.together_english.deiz.security.service

import com.together_english.deiz.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
        private val memberRepository: MemberRepository
) : UserDetailsService{

    private final val LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl::class.java)

    override fun loadUserByUsername(username: String): UserDetails {
        LOGGER.info("[loadUserByUsername] loadUserByUsername 수행. username : {}", username)
        return memberRepository.findByEmail(username).get()
    }

}