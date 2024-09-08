package com.together_english.deiz.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.together_english.deiz.exception.GlobalHandler
import com.together_english.deiz.exception.UserAlreadyExistException
import com.together_english.deiz.model.member.dto.SignUpRequest
import com.together_english.deiz.service.AuthService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@DisplayName("인증 컨트롤러 테스트")
@ExtendWith(MockitoExtension::class)
class AuthControllerTest{

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var authService: AuthService

    @InjectMocks
    private lateinit var authController: AuthController

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(GlobalHandler())
                .build()
    }

    @DisplayName("일반 회원가입 성공 테스트")
    @Test
    fun signUpSuccessTest() {
        val signUpRequest = SignUpRequest(
                name = "테스트",
                nickname = "닉네임",
                email = "test@example.com",
                password = "password123"
        )
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
        val signUpRequest = SignUpRequest(
                name = "테스트",
                nickname = "닉네임",
                email = "duplicate@example.com",
                password = "password123"
        )

        doThrow(UserAlreadyExistException::class.java).`when`(authService).signUp(signUpRequest)

        mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapper().writeValueAsString(signUpRequest))
        )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.status").value("error"))
    }


}