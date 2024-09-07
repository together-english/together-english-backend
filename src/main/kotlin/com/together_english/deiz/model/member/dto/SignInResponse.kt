package com.together_english.deiz.model.member.dto

import com.together_english.deiz.model.common.JwtToken

class SignInResponse(
        val memberDto: MemberDto,
        val jwtToken: JwtToken
)