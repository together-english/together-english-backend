package com.together_english.deiz.controller

import com.together_english.deiz.model.common.MainResponse
import com.together_english.deiz.model.common.MainResponse.Companion.getSuccessResponse
import com.together_english.deiz.model.member.dto.ResetPasswordRequest
import com.together_english.deiz.model.member.dto.SignUpRequest
import com.together_english.deiz.model.member.dto.SignInRequest
import com.together_english.deiz.model.member.dto.SignInResponse
import com.together_english.deiz.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders


@RestController
@RequestMapping("/auth")
@Tag(name = "인증 API", description = "유저 인증을 위한 API")
class AuthController(
        private val authService: AuthService,
        @Value("\${auth.password.reset.uri:/default-reset-uri}")
        private val passwordResetURI: String,
) {

    @Operation(summary = "회원 가입", description = "회원가입 요청")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation completed successfully."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<MainResponse<Nothing>> {
        authService.signUp(signUpRequest)
        return ResponseEntity.ok(getSuccessResponse())
    }

    @Operation(summary = "로그인", description = "로그인 요청")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation completed successfully."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @PostMapping("/login")
    fun signIn(@Valid @RequestBody signInRequest: SignInRequest): ResponseEntity<MainResponse<SignInResponse>> {
        val signInResponse = authService.signIn(signInRequest)

        val refreshTokenCookie = Cookie("refresh_token", signInResponse.jwtToken.refreshToken)
        refreshTokenCookie.isHttpOnly = true
        refreshTokenCookie.path = "/"
        refreshTokenCookie.maxAge = 60 * 60 * 24 * 1

        val headers = HttpHeaders()
        val cookieString = StringBuilder()
            .append("refresh_token=${refreshTokenCookie.value}")
            .append("; HttpOnly")
            .append("; Path=${refreshTokenCookie.path}")
            .append("; Max-Age=${refreshTokenCookie.maxAge}")
            .toString()
        headers.add(HttpHeaders.SET_COOKIE, cookieString)

        return ResponseEntity.ok().headers(headers).body(getSuccessResponse(signInResponse))
    }

    @Operation(
        summary = "비밀번호 재설정을 위한 이메일 발송",
        description = "비밀번호 재설정을 위한 이메일을 발송 합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation completed successfully."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @PostMapping("/password/reset/mail")
    fun sendPasswordResetEmail(
        @RequestParam email: String,
    ): ResponseEntity<MainResponse<Nothing>> {
        authService.sendPasswordResetEmail(email, passwordResetURI)
        return ResponseEntity.ok(MainResponse.getSuccessResponse())
    }

    @Operation(
        summary = "나의 비밀번호 재설정",
        description = "나의 비밀번호를 재설정 합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation completed successfully."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @PatchMapping("/password/reset")
    fun resetPassword(
        @Valid @RequestBody request: ResetPasswordRequest
    ): ResponseEntity<MainResponse<Nothing>> {
        authService.resetPassword(request)
        return ResponseEntity.ok(MainResponse.getSuccessResponse())
    }
}