package com.together_english.deiz.service

import com.together_english.deiz.data.JwtToken
import com.together_english.deiz.data.member.dto.MemberDto
import com.together_english.deiz.data.member.dto.SignInRequest
import com.together_english.deiz.data.member.dto.SignInResponse
import com.together_english.deiz.data.member.dto.SignUpRequest
import com.together_english.deiz.data.member.entity.Member
import com.together_english.deiz.exception.UserAlreadyExistException
import com.together_english.deiz.repository.MemberRepository
import com.together_english.deiz.security.util.JwtUtil
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
        private val memberRepository: MemberRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtUtil: JwtUtil
) {

    fun signUp(signUpRequest: SignUpRequest) {
        if (memberRepository.findByEmail(signUpRequest.email).isPresent) {
            throw UserAlreadyExistException()
        }
        val encodedPassword = passwordEncoder.encode(signUpRequest.password)
        val member = Member(
                name = signUpRequest.name,
                email = signUpRequest.email,
                hashedPassword = encodedPassword,
                profile = signUpRequest.profile,
                nickname = signUpRequest.nickname ?: signUpRequest.name
        )
        memberRepository.save(member)
    }

    fun signIn(signInRequest: SignInRequest): SignInResponse {
        val member = memberRepository.findByEmail(signInRequest.email).orElseThrow {
            UsernameNotFoundException("해당 유저가 존재하지 않습니다.")
        }
        if (!passwordEncoder.matches(signInRequest.password, member.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }
        return SignInResponse(
                memberDto = MemberDto.memberToDto(member),
                jwtToken = jwtUtil.generateAllToken(member.email)
        )
    }
}