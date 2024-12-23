package com.together_english.deiz.controller

import com.together_english.deiz.model.common.MainResponse
import com.together_english.deiz.model.member.dto.MyCreatedCirclePageResponse
import com.together_english.deiz.model.member.dto.MyJoinedCirclePageResponse
import com.together_english.deiz.model.member.dto.MyPageResponse
import com.together_english.deiz.model.member.dto.MyPageUpdateRequest
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/member")
@Tag(name = "유저 API", description = "유저 정보 조회 및 업데이트를 위한 API")
class MemberController(
    private val memberService: MemberService
) {

    @Operation(
        summary = "프로파일 수정",
        description = "유저의 프로파일을 수정합니다. data 값에 업데이트된 프로필 url이 return됩니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operation completed successfully."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @PostMapping("/profile", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun changeProfile(
        @RequestParam("file") file: MultipartFile,
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<String>> {
        val updatedProfile = memberService.updateMemberProfile(file, member.email)
        return ResponseEntity.ok(MainResponse.getSuccessResponse(updatedProfile))
    }

    @Operation(
        summary = "나의 정보 조회",
        description = "나의 정보를 조회합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operation completed successfully."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @GetMapping("/my")
    fun getMyInfo(
        @Parameter(hidden = true) member: Member
    ): ResponseEntity<MainResponse<MyPageResponse>> {
        val response: MyPageResponse = memberService.getMyInfo(member.email)
        return ResponseEntity.ok(MainResponse.getSuccessResponse(response))
    }

    @Operation(
        summary = "나의 정보 업데이트",
        description = "나의 정보를 업데이트 합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operation completed successfully."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @PostMapping("/my")
    fun updateMyInfo(
        @Parameter(hidden = true) member: Member,
        @RequestBody request: MyPageUpdateRequest
    ): ResponseEntity<MainResponse<Nothing>> {
        memberService.updateMyInfo(member.email, request)
        return ResponseEntity.ok(MainResponse.getSuccessResponse())
    }

    @Operation(
        summary = "내 가입모임 정보 목록조회",
        description = "내가 가입한 모임의 정보 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operation completed successfully."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @GetMapping("/my/joined-circles")
    fun getMyJoinedCircleList(
        @PageableDefault(
            size = 10, page = 0
        ) pageable: Pageable,
        @Parameter(hidden = true) member: Member
    ) : ResponseEntity<MainResponse<Page<MyJoinedCirclePageResponse?>>>{
        val myJoinedCircleList = memberService.getMyJoinedCircleList(member, pageable)
        return ResponseEntity.ok(MainResponse.getSuccessResponse(myJoinedCircleList))
    }

    @Operation(
        summary = "내 창설모임 정보 목록조회",
        description = "내가 창설한 모임의 정보 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operation completed successfully."),
            ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
        ]
    )
    @GetMapping("/my/created-circles")
    fun getMyCreatedCircleList(
        @PageableDefault(
            size = 10, page = 0
        ) pageable: Pageable,
        @Parameter(hidden = true) member: Member
    ) : ResponseEntity<MainResponse<Page<MyCreatedCirclePageResponse?>>>{
        val myCreatedCircleList = memberService.getMyCreatedCircleList(member, pageable)
        return ResponseEntity.ok(MainResponse.getSuccessResponse(myCreatedCircleList))
    }

}