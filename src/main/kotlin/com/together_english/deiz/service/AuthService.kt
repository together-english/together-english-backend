package com.together_english.deiz.service

import com.together_english.deiz.data.member.dto.SignInDto
import com.together_english.deiz.data.member.dto.SignUpDto
import com.together_english.deiz.data.member.entity.Member
import com.together_english.deiz.data.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
        private val memberRepository: MemberRepository,
        private val passwordEncoder: PasswordEncoder
) {

    fun signUp(signUpDto: SignUpDto) {
        val encodedPassword = passwordEncoder.encode(signUpDto.password)
        val member = Member(
                name = signUpDto.name,
                email = signUpDto.email,
                password = encodedPassword,
                profile = signUpDto.profile
        )
        memberRepository.save(member)
    }

    fun signIn(signInDto: SignInDto): String {
        val member = memberRepository.findByEmail(signInDto.email).orElseThrow {
            UsernameNotFoundException("해당 유저가 존재하지 않습니다.")
        }

        if (!passwordEncoder.matches(signInDto.password, member.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }

        // 인증이 성공했을 때 JWT 토큰을 발급하는 로직 추가
        return "dummy-token" // 실제로는 JWT 토큰을 반환해야 함
    }
}