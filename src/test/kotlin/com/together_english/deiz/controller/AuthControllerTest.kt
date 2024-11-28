package com.together_english.deiz.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.together_english.deiz.exception.GlobalHandler
import com.together_english.deiz.model.member.dto.SignUpRequest
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.MemberAuthRepository
import com.together_english.deiz.repository.MemberRepository
import com.together_english.deiz.security.util.JwtUtil
import com.together_english.deiz.service.AuthService
import com.together_english.deiz.service.MailApiService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

@DisplayName("인증 컨트롤러 테스트")
@ExtendWith(MockitoExtension::class)
class AuthControllerTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var memberRepository: MemberRepository

    @Mock
    private lateinit var memberAuthRepository: MemberAuthRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var jwtUtil: JwtUtil

    @Mock
    private lateinit var mailApiService: MailApiService

    private val passwordResetURI = "https://example.com/reset-password" // 직접 초기화

    @InjectMocks
    private lateinit var authService: AuthService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(AuthController(authService, passwordResetURI))
            .setControllerAdvice(GlobalHandler())
            .build()
    }

    // 공통적으로 사용될 테스트 데이터 생성 메서드
    private fun createSignUpRequest(
        name: String = "테스트",
        nickname: String = "닉네임",
        email: String = "test@example.com",
        password: String = "password123",

        ): SignUpRequest {
        return SignUpRequest(
            name = name,
            nickname = nickname,
            email = email,
            password = password,
            isTermsAgreed = true,
            isPrivacyAgreed = true
        )
    }

    private fun createTestMember(
        name: String = "test",
        nickname: String = "test",
        email: String = "test@test.com",
        password: String = "hashedPassword",
        isTermsAgreed: Boolean = true,
        isPrivacyAgreed: Boolean = true
    ): Member {
        return Member(
            name = name,
            nickname = nickname,
            email = email,
            hashedPassword = password,
            isTermsAgreed = isTermsAgreed,
            isPrivacyAgreed = isPrivacyAgreed
        )
    }

    @DisplayName("일반 회원가입 성공 테스트")
    @Test
    fun signUpSuccessTest() {
        val signUpRequest = createSignUpRequest()
        `when`(passwordEncoder.encode(signUpRequest.password)).thenReturn("encodedPassword")

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(signUpRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("success"))
    }

    @DisplayName("이메일 중복으로 인한 회원가입 실패 테스트")
    @Test
    fun failSignUpTestDueToDuplicateEmail() {
        val testMember = createTestMember()
        val signUpRequest = createSignUpRequest(email = "duplicate@example.com")

        `when`(memberRepository.findByEmail(signUpRequest.email)).thenReturn(Optional.of(testMember))

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(signUpRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("error"))
    }

    @DisplayName("닉네임 중복으로 인한 회원가입 실패 테스트")
    @Test
    fun failSignUpTestDueToDuplicateNickname() {
        val testMember = createTestMember()
        val signUpRequest = createSignUpRequest(nickname = "duplicateNickname")

        `when`(memberRepository.findByNickname(signUpRequest.nickname!!)).thenReturn(Optional.of(testMember))

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(signUpRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("error"))
    }

}
