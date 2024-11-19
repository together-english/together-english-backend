package com.together_english.deiz.service

import com.together_english.deiz.exception.NicknameAlreadyInUseException
import com.together_english.deiz.exception.NotExistException
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.exception.UserAlreadyExistException
import com.together_english.deiz.exception.UserNotFoundException
import com.together_english.deiz.model.member.dto.*
import com.together_english.deiz.model.member.entity.MemberAuth
import com.together_english.deiz.repository.MemberAuthRepository
import com.together_english.deiz.repository.MemberRepository
import com.together_english.deiz.security.util.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.UUID.randomUUID

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val memberAuthRepository: MemberAuthRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val mailApiService: MailApiService,

    @Value("\${auth.password.reset.uri:/default-reset-uri}")
    private val passwordResetURI: String,
) {

    private fun checkUserExistsByEmailOrNickname(email: String, nickname: String?) {
        if (memberRepository.findByEmail(email).isPresent) {
            throw UserAlreadyExistException("해당 이메일을 가진 유저가 이미 존재합니다.")
        }
        if (nickname != null) {
            if (memberRepository.findByNickname(nickname).isPresent) {
                throw NicknameAlreadyInUseException()
            }
        }
    }

    @Transactional
    fun signUp(signUpRequest: SignUpRequest) {
        checkUserExistsByEmailOrNickname(signUpRequest.email, signUpRequest.nickname)
        val encodedPassword = passwordEncoder.encode(signUpRequest.password)
        val member = Member(
            name = signUpRequest.name,
            email = signUpRequest.email,
            hashedPassword = encodedPassword,
            profile = signUpRequest.profile,
            nickname = signUpRequest.nickname ?: signUpRequest.name,
            isPrivacyAgreed = signUpRequest.isPrivacyAgreed,
            isTermsAgreed = signUpRequest.isTermsAgreed,
            isMarketingAgreed = signUpRequest.isMarketingAgreed
        )
        memberRepository.save(member)
    }

    fun signIn(signInRequest: SignInRequest): SignInResponse {
        val member = memberRepository.findByEmail(signInRequest.email).orElseThrow {
            UserNotFoundException()
        }
        member.checkPasswordIsMatched(signInRequest.password, passwordEncoder)
        return SignInResponse(
            memberDto = MemberDto.memberToDto(member),
            jwtToken = jwtUtil.generateAllToken(member.email)
        )
    }

    @Transactional
    fun sendPasswordResetEmail(email: String) {
        val user =
            memberRepository.findByEmail(email).orElseThrow { UsernameNotFoundException("$email: 해당 유저가 존재하지 않습니다.") }
        val userName = user.name
        val authKey = randomUUID()
        //TODO : 프론트 리다이렉트 화면으로 sendURL 변경(application yml)
        val sendURL = "$passwordResetURI?UUID=${authKey.toString()}"

        val mailSendResponse = mailApiService.callSendPasswordResetEmail(email, userName, sendURL)
        if (mailSendResponse.status != HttpStatus.OK.value()) {
            throw RuntimeException("메일 송신이 실패하였습니다. 다시 시도해주세요.")
        }

        val memberAuth: MemberAuth = MemberAuth(
            email = email,
            authKey = authKey
        )
        memberAuthRepository.save(memberAuth)
    }

    @Transactional
    fun resetPassword(request: ResetPasswordRequest) {
        val authKey: UUID = UUID.fromString(request.authKey)
        val memberAuth = memberAuthRepository.findByAuthKey(authKey).orElseThrow {
            NotExistException("비밀번호 변경 요청 기록")
        }

        val member = memberRepository.findByEmail(memberAuth.email).orElseThrow {
            UsernameNotFoundException("${memberAuth.email}: 해당 유저가 존재하지 않습니다.")
        }

        val encodedPassword = passwordEncoder.encode(request.newPassword)
        // TODO : 사용자 인증 테이블에 유효기간 관련 컬럼 설정?
        member.updatePassword(encodedPassword)
    }
}