package com.together_english.deiz.controller

import com.together_english.deiz.model.circle.dto.CommentCreateRequest
import com.together_english.deiz.model.circle.dto.CommentUpdateRequest
import com.together_english.deiz.model.common.MainResponse
import com.together_english.deiz.model.common.MainResponse.Companion.getSuccessResponse
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.service.CommentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/comment")
@Tag(name = "댓글 API", description = "댓글 생성/수정/삭제를 위한 API")
class CommentController(private val commentService: CommentService) {
    @Operation(
        summary = "댓글 생성",
        description = "댓글을 생성합니다. 반환 값(SUCCESS/FAIL)",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Comment created : SUCCESS"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data."),
        ]
    )
    @PostMapping()
    fun createCircleComment(
        @Valid @RequestBody request: CommentCreateRequest,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        val commentId = commentService.createCircleComment(request, member)
        return ResponseEntity.ok(getSuccessResponse("Comment created : $commentId"))
    }

    @Operation(
        summary = "댓글 업데이트",
        description = "댓글을 업데이트합니다. 반환 값(SUCCESS/FAIL)",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Comment updated : SUCCESS"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data."),
        ]
    )
    @PatchMapping()
    fun updateCircleComment(
        @Valid @RequestBody request: CommentUpdateRequest,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        val commentId = commentService.updateCircleComment(request, member)
        return ResponseEntity.ok(getSuccessResponse("Comment updated : $commentId"))
    }

}