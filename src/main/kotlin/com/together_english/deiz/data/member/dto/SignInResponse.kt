package com.together_english.deiz.data.member.dto

import com.together_english.deiz.data.JwtToken

class SignInResponse(
        val memberDto: MemberDto,
        val jwtToken: JwtToken
)