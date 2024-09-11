package com.together_english.deiz.service

import com.together_english.deiz.model.member.dto.MemberDto
import com.together_english.deiz.model.member.dto.SignInRequest
import com.together_english.deiz.model.member.dto.SignInResponse
import com.together_english.deiz.model.member.dto.SignUpRequest
import com.together_english.deiz.model.member.entity.Member
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

    private fun checkUserExistsByEmailOrNickname(email: String, nickname: String?) {
        if (memberRepository.findByEmail(email).isPresent) {
            throw UserAlreadyExistException("해당 이메일을 가진 유저가 이미 존재합니다.")
        }
        if (nickname != null) {
            if (memberRepository.findByNickname(nickname).isPresent) {
                throw UserAlreadyExistException("해당 닉네임을 가진 유저가 이미 존재합니다.")
            }
        }
    }

    fun signUp(signUpRequest: SignUpRequest) {
        checkUserExistsByEmailOrNickname(signUpRequest.email, signUpRequest.nickname)
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
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }
        return SignInResponse(
                memberDto = MemberDto.memberToDto(member),
                jwtToken = jwtUtil.generateAllToken(member.email)
        )
    }
}