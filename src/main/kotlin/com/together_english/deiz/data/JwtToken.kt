package com.together_english.deiz.data

import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "JWT Token")
data class JwtToken(
        @Schema(description = "access token", example = "euwifhwefuiwerfweuirhui32423423432")
        val accessToken: String,
        @Schema(description = "refresh token", example = "eurdfhu23ihru23i4hr32ui4rh3ui24rh")
        val refreshToken: String
)