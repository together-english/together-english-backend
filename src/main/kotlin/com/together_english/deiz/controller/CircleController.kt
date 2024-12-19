package com.together_english.deiz.controller

import com.together_english.deiz.model.circle.dto.*
import com.together_english.deiz.model.common.MainResponse
import com.together_english.deiz.model.common.MainResponse.Companion.getSuccessResponse
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.service.CircleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID


@RestController
@RequestMapping("/circle")
@Tag(name = "영어 모임 API", description = "영어 모임을 위한 API")
class CircleController(
    private val circleService: CircleService
) {

    @Operation(
        summary = "영어 모임 생성",
        description = "영어 모임을 생성합니다. 반환 값으로 모임의 ID 값이 포함됩니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Circle created successfully with ID: 2132"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createCircle(
        @Valid @RequestPart request: CircleCreateRequest,
        @RequestPart(required = false) thumbnailFile: MultipartFile?,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        val circleId = circleService.createCircleWithSchedule(request, member, thumbnailFile)
        return ResponseEntity.ok(getSuccessResponse("Circle created successfully with ID: $circleId"))
    }

    @Operation(
        summary = "영어 모임 목록 페이지네이션 조회",
        description = "영어 모임 목록을 페이징하여 반환합니다.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved circle pages with schedules."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @PostMapping("/list")
    fun findCirclesByPagination(
        @PageableDefault(
            size = 9, page = 0, sort = ["createdAt"], direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @Valid @RequestBody request: CircleSearchRequest,
    ): ResponseEntity<MainResponse<Page<CirclePageResponse?>>> {
        val request =
            CircleSearchRequest(request.memberId, request.title, request.city, request.level, request.likeByMeOnly)
        val circlesPage = circleService.findCirclesByPagination(pageable, request)
        return ResponseEntity.ok(MainResponse.getSuccessResponse(circlesPage))
    }

    @Operation(
        summary = "영어 모임 업데이트",
        description = "영어 모임을 업데이트 합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Circle updated successfully with ID: 1"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @PutMapping
    fun updateCircle(
        @Valid @RequestBody request: CircleUpdateRequest,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        circleService.updateCircleWithSchedule(
            request = request,
            member = member
        )
        return ResponseEntity.ok(getSuccessResponse("Circle updated successfully with ID: ${request.id}"))
    }

    @Operation(
        summary = "영어 모임 삭제하기",
        description = "영어 모임을 삭제합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Circle deleted successfully with ID: 1"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteCircle(
        @PathVariable id: UUID,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        circleService.deleteCircleWithSchedule(id, member)
        return ResponseEntity.ok(getSuccessResponse("Circle deleted successfully with ID: $id"))
    }

    @Operation(
        summary = "영어 모임 좋아요",
        description = "영어 모임 좋아요(찜)를 합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Circle Favorite Add successfully"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @PostMapping("{id}/favorites")
    fun addFavoriteToCircle(
        @PathVariable id: UUID,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        circleService.addFavoriteToCircle(id, member)
        return ResponseEntity.ok(getSuccessResponse("Circle Favorite Add successfully with Circle Id: $id"))
    }

    @Operation(
        summary = "영어 모임 좋아요 취소",
        description = "영어 모임 좋아요(찜)를 취소합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Circle Favorite deleted successfully"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @DeleteMapping("{id}/favorites")
    fun removeFavoriteToCircle(
        @PathVariable id: UUID,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        circleService.removeFavoriteToCircle(id, member)
        return ResponseEntity.ok(getSuccessResponse("Circle Favorite deleted successfully with Circle Id: $id"))
    }


    @Operation(
        summary = "영어 모임 상세 조회",
        description = "영어 모임을 상세 조회 합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved circle detail with schedules."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data")
        ]
    )
    @GetMapping("/detail/{id}")
    fun getCircleDetail(
        @PathVariable id: UUID
    ): ResponseEntity<MainResponse<CircleDetailResponse>> {
        val response = circleService.getCircleDetail(id)
        return ResponseEntity.ok(getSuccessResponse(response))
    }

    @Operation(
        summary = "영어 모임 참가 요청",
        description = "영어 모임에 참가요청을 합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Circle Join Request successfully"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @PostMapping("/join-requests")
    fun submitCircleJoinRequest(
        @Valid @RequestBody request: CircleJoinCreateRequestDTO,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        circleService.submitCircleJoinRequest(request, member)
        return ResponseEntity.ok(getSuccessResponse("Circle Join Request successfully with Circle Id: ${request.circleId}, Member Id: ${member.id}"))
    }

    @Operation(
        summary = "영어모임 신청한 사용자 리스트 조회",
        description = "영어모임을 신청한 사용자들의 리스트를 조회합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved circle join request member list."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data")
        ]
    )
    @GetMapping("{circleId}/join-requests")
    fun findCircleJoinRequestList(
        @PathVariable circleId: UUID,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<List<CircleJoinDetailResponse>>> {
        val circleJoinResponses = circleService.findCircleJoinRequestList(circleId, member)
        return ResponseEntity.ok(getSuccessResponse(circleJoinResponses))
    }

    @Operation(
        summary = "영어모임 신청한 사용자 신청정보 상세조회",
        description = "영어모임을 신청한 사용자의 신청정보를 상세 조회합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved circle join request member."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data")
        ]
    )
    @GetMapping("/join-requests/{circleJoinRequestId}")
    fun findCircleJoinRequestDetail(
        @PathVariable circleJoinRequestId: UUID,
    ): ResponseEntity<MainResponse<CircleJoinDetailResponse>> {
        val circleJoinResponse = circleService.findCircleJoinRequestDetail(circleJoinRequestId)
        return ResponseEntity.ok(getSuccessResponse(circleJoinResponse))
    }

    @Operation(
        summary = "영어 모임 참가 요청 수정",
        description = "영어 모임에 참가요청 사항을 수정합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Circle Join Request updated successfully"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @PatchMapping("/join-requests")
    fun updateCircleJoinRequest(
        @Valid @RequestBody request: CircleJoinUpdateRequestDTO,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        val circleJoinRequestId = circleService.updateCircleJoinRequest(request, member)
        return ResponseEntity.ok(getSuccessResponse("Circle Join Request successfully updated : $circleJoinRequestId"))
    }

}