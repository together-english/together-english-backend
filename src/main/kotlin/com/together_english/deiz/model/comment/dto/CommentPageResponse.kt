package com.together_english.deiz.model.comment.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

@Schema(description = "댓글 페이지 조회 응답")
data class CommentPageResponse(
    @Schema(description = "댓글 ID", example = "042482cb-f1cd-4935-9579-e12da625961f")
    val id: UUID,
    @Schema(description = "댓글 업데이트 시간", example = "2024-11-10 20:47:23")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime,
    @Schema(description = "댓글 내용", example = "테스트 댓글 내용")
    val content: String,
    @Schema(description = "서클 ID", example = "12e2156d-f4cb-4ddb-be3a-71e42da76ds2")
    val circleId: UUID,
    @Schema(description = "유저 닉네임", example = "창원불주먹종와이")
    @JsonProperty("nickname")
    val memberNickname: String,
    @Schema(description = "유저 프로필", example = "URL")
    @JsonProperty("profile")
    val memberProfile: String,
) {

}