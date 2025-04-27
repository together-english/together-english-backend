package com.together_english.deiz.controller

import com.together_english.deiz.model.comment.dto.CommentCreateRequest
import com.together_english.deiz.model.comment.dto.CommentPageResponse
import com.together_english.deiz.model.comment.dto.CommentUpdateRequest
import com.together_english.deiz.model.common.MainResponse
import com.together_english.deiz.model.common.MainResponse.Companion.getSuccessResponse
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.service.CommentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

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
        summary = "댓글 목록 조회",
        description = "영어 모임의 댓글 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data."),
        ]
    )
    @GetMapping
    fun findCommentsByPagination(
        @PageableDefault(
            size = 10, page = 0, sort = ["updatedAt"], direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @RequestParam(required = true)
        @Schema(example = "042482cb-f1cd-4935-9579-e12da625961f")
        circleId: UUID,
    ): ResponseEntity<MainResponse<Page<CommentPageResponse?>>> {
        val commentPage = commentService.findCommentsByPagination(pageable, circleId)
        return ResponseEntity.ok(MainResponse.getSuccessResponse(commentPage))
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
    @PutMapping()
    fun updateCircleComment(
        @Valid @RequestBody request: CommentUpdateRequest,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        val commentId = commentService.updateCircleComment(request, member)
        return ResponseEntity.ok(getSuccessResponse("Comment updated : $commentId"))
    }

    @Operation(
        summary = "댓글 삭제(상태변경)",
        description = "댓글을 삭제(상태변경)합니다. 반환 값(SUCCESS/FAIL)",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Comment deleted : SUCCESS"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data."),
        ]
    )
    @DeleteMapping("/{commentId}")
    fun deleteCircleComment(
        @PathVariable(required = true) commentId: UUID,
        @Parameter(hidden = true) member: Member,
    ): ResponseEntity<MainResponse<String>> {
        commentService.deleteCircleComment(commentId, member)
        return ResponseEntity.ok(getSuccessResponse("Comment deleted : $commentId"))
    }
}