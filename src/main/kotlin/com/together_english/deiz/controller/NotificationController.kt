package com.together_english.deiz.controller

import com.together_english.deiz.model.common.MainResponse
import com.together_english.deiz.model.common.MainResponse.Companion.getSuccessResponse
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.model.notification.dto.NotificationPageResponse
import com.together_english.deiz.service.NotificationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/notification")
@Tag(name = "알림 API", description = "알림 관련 API")
class NotificationController(
    private val notificationService: NotificationService
) {

    @Operation(
        summary = "사용자 알림 목록 조회",
        description = "사용자의 알림 목록을 조회합니다. 조회 조건으로 읽음 여부와 마지막 생성 시간을 지정할 수 있습니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved notifications"),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data")
        ]
    )
    @GetMapping
    fun findNotifications(
        @Parameter(hidden = true) member: Member,
        @RequestParam(required = false) viewed: Boolean?,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        lastCreatedAt: LocalDateTime?,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<MainResponse<NotificationPageResponse>> {
        val notifications = notificationService.findNotificationsByMemberIdAfterCreatedAt(
            member = member,
            viewed = viewed,
            lastCreatedAt = lastCreatedAt,
            size = size
        )
        return ResponseEntity.ok(getSuccessResponse(notifications))
    }
}