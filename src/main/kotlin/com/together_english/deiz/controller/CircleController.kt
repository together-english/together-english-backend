package com.together_english.deiz.controller

import com.together_english.deiz.model.circle.dto.CircleCreateRequest
import com.together_english.deiz.model.circle.dto.CirclePageResponse
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
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Circle created successfully with ID: 2132"),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
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
            description = "영어 모임 목록을 페이징하여 반환합니다."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved circles with schedules."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @GetMapping
    fun findCirclesByPagination(
            @PageableDefault(
                    size = 9, page = 0, sort = ["createdAt"], direction = Sort.Direction.DESC
            ) pageable: Pageable
    ): ResponseEntity<MainResponse<Page<CirclePageResponse?>>> {
        val circlesPage = circleService.findCirclesByPagination(pageable)
        return ResponseEntity.ok(MainResponse.getSuccessResponse(circlesPage))
    }

}