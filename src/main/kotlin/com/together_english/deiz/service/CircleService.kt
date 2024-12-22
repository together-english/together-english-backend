package com.together_english.deiz.service

import com.together_english.deiz.exception.AlreadyExistException
import com.together_english.deiz.exception.NotExistException
import com.together_english.deiz.exception.UnAuthorizedAccessException
import com.together_english.deiz.model.circle.CircleJoinStatus
import com.together_english.deiz.model.circle.CircleMember
import com.together_english.deiz.model.circle.FavoriteCircle
import com.together_english.deiz.model.circle.dto.*
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class CircleService(
    private val circleRepository: CircleRepository,
    private val circleJoinRepository: CircleJoinRepository,
    private val circleMemberRepository: CircleMemberRepository,
    private val s3ImageUploadService: S3ImageUploadService,
    private val circleScheduleRepository: CircleScheduleRepository,
    private val favoriteCircleRepository: FavoriteCircleRepository,
) {

    @Transactional
    fun createCircleWithSchedule(request: CircleCreateRequest, member: Member, thumbnailFile: MultipartFile?): String {
        val thumbnailUrl = thumbnailFile?.let { s3ImageUploadService.uploadFile(it) }
        val circle = request.toEntity(member, thumbnailUrl)
        circleRepository.save(circle)

        val circleScheduleList = request.circleSchedules.map { scheduleRequest ->
            scheduleRequest.toEntity(scheduleRequest, circle)
        }
        circleScheduleRepository.saveAll(circleScheduleList)

        val circleMemberToSave = CircleMember(
            member = member,
            circle = circle,
        )
        circleMemberToSave.updateStatus(CircleMember.CircleRole.LEADER)
        circleMemberRepository.save(circleMemberToSave)

        return circle.id.toString()
    }

    @Transactional
    fun updateCircleWithSchedule(request: CircleUpdateRequest, member: Member) {
        val circle = circleRepository.findById(request.id).orElseThrow {
            NotExistException("circle id: $request.id")
        }
        if (circle.leader.id != member.id) {
            throw UnAuthorizedAccessException()
        }
        circle.update(request)
        if (request.circleSchedules.isNotEmpty()) {
            val circleSchedule = request.circleSchedules.map { it.toEntity(it, circle) }
            circleScheduleRepository.deleteAllByCircle(circle)
            circleScheduleRepository.saveAll(circleSchedule)
        }
    }

    @Transactional
    fun deleteCircleWithSchedule(id: UUID, member: Member) {
        var circle = circleRepository.findById(id).orElseThrow {
            NotExistException("circle id: $id")
        }
        if (circle.leader.id != member.id) {
            throw UnAuthorizedAccessException()
        }
        circle.delete()
    }

    @Transactional
    fun addFavoriteToCircle(id: UUID, member: Member) {
        val circle = circleRepository.findById(id).orElseThrow { NotExistException("circle id: $id") }
        if (!circle.valid) {
            throw NotExistException("circle id: ${circle.id}")
        }

        val existFavoriteCircle = favoriteCircleRepository.findByCircleAndMember(circle, member)
        if (existFavoriteCircle != null) {
            throw AlreadyExistException("모임 좋아요 기록(circle id: ${existFavoriteCircle.circle.id})")
        }

        val favoriteCircle = FavoriteCircle(circle, member)
        favoriteCircleRepository.save(favoriteCircle)
    }

    @Transactional
    fun removeFavoriteToCircle(id: UUID, member: Member) {
        val circle = circleRepository.findById(id).orElseThrow { NotExistException("circle id: $id") }
        val favoriteCircle = favoriteCircleRepository.findByCircleAndMember(circle, member)
            ?: throw NotExistException("모임 좋아요 기록(circle id: ${id})")

        favoriteCircleRepository.delete(favoriteCircle)
    }

    @Transactional(readOnly = true)
    fun getCircleDetail(id: UUID): CircleDetailResponse {
        val circle = circleRepository.findById(id).orElseThrow { NotExistException("circle id: $id") }
        return CircleDetailResponse.fromEntity(circle)
    }

    @Transactional
    fun submitCircleJoinRequest(request: CircleJoinCreateRequestDTO, member: Member) {
        val circle = circleRepository.findById(request.circleId)
            .orElseThrow { NotExistException("circle id: ${request.circleId}") }

        val circleMember = circleMemberRepository.findByCircleAndMember(circle, member)
        require(circleMember.isEmpty) { "이미 모임에 가입된 회원입니다. circle id : ${request.circleId}" }

        val existJoinRequest = circleJoinRepository.existsByCircleIdAndMemberId(circle.id, member.id)
        if (existJoinRequest) {
            throw AlreadyExistException("모임신청 요청")
        }

        val circleJoinRequest = request.toEntity(circle, member)
        circleJoinRepository.save(circleJoinRequest)
    }

    fun findCircleJoinRequestList(circleId: UUID, member: Member): List<CircleJoinDetailResponse> {
        val circle = circleRepository.findById(circleId).orElseThrow { NotExistException("circle id: $circleId") }
        require(circle.isWrittenBy(member)) { "리더만 가입요청 조회가 가능합니다." }

        val circleJoinResponses = circleJoinRepository.findByCircle(circle)
        val circleJoinResponseList =
            circleJoinResponses.map { it.toCircleJoinDetailResponse() }

        return circleJoinResponseList
    }

    fun findCircleJoinRequestDetail(circleJoinRequestId: UUID): CircleJoinDetailResponse {
        val circleJoinDetailResponse = circleJoinRepository.findCircleJoinRequestDetail(circleJoinRequestId)
            ?: throw NotExistException("모임 신청 요청 기록이")

        return circleJoinDetailResponse
    }

    @Transactional
    fun updateCircleJoinRequest(request: CircleJoinUpdateRequestDTO, member: Member): String {
        val circleJoinRequest = circleJoinRepository.findById(request.circleJoinRequestId)
            .orElseThrow { NotExistException("circle join request id: ${request.circleJoinRequestId}") }

        require(circleJoinRequest.isWrittenBy(member)) { "모임 신청한 본인 외 수정은 불가능합니다." }
        require(circleJoinRequest.status == CircleJoinStatus.WAITING) { "모임 가입요청이 대기상태인 경우만 수정 가능합니다. 현재 상태: ${circleJoinRequest.status}" }

        circleJoinRequest.updateMessage(request)
        return circleJoinRequest.id.toString()
    }

    @Transactional
    fun approveCircleJoinRequest(circleJoinRequestId: UUID, member: Member): String {
        val circleJoinRequest = circleJoinRepository.findById(circleJoinRequestId)
            .orElseThrow { NotExistException("circle join request id: $circleJoinRequestId") }

        val circleMember = circleMemberRepository.findByCircleAndMember(circleJoinRequest.circle, member)
            .orElseThrow { Exception("해당 모임의 팀원이 아닙니다. 가입요청 승인이 불가능합니다.") }

        require(circleMember.role == CircleMember.CircleRole.LEADER) { "모임 리더권한 유저만 모임 가입요청 승인이 가능합니다." }
        require(circleJoinRequest.status == CircleJoinStatus.WAITING) { "모임 가입요청이 대기상태인 경우만 승인 가능합니다. 현재 상태: ${circleJoinRequest.status}" }

        val circleMemberToSave = circleJoinRequest.toCircleMemberEntity()
        circleMemberRepository.save(circleMemberToSave)
        circleJoinRequest.updateStatus(CircleJoinStatus.ACCEPTED)

        return circleMemberToSave.id.toString()
    }

    @Transactional
    fun rejectCircleJoinRequest(circleJoinRequestId: UUID, member: Member) {
        val circleJoinRequest = circleJoinRepository.findById(circleJoinRequestId)
            .orElseThrow { NotExistException("circle join request id: $circleJoinRequestId") }

        val circleMember = circleMemberRepository.findByCircleAndMember(circleJoinRequest.circle, member)
            .orElseThrow { Exception("해당 모임의 팀원이 아닙니다. 가입요청 거절이 불가능합니다.") }

        require(circleMember.role == CircleMember.CircleRole.LEADER) { "모임 리더권한 유저만 모임 가입요청 거절이 가능합니다." }
        require(circleJoinRequest.status == CircleJoinStatus.WAITING) { "모임 가입요청이 대기상태인 경우만 거절 가능합니다. 현재 상태: ${circleJoinRequest.status}" }

        circleJoinRequest.updateStatus(CircleJoinStatus.REJECTED)
    }

    fun findMemberByCircleList(circleId: UUID, member: Member, pageable: Pageable): Page<CircleMemberPageResponse?> {
        val isCircleMember = circleMemberRepository.existsByCircleIdAndMemberId(circleId, member.id)
        require(isCircleMember) { "모임에 가입된 멤버만 조회 가능합니다. circleId : $circleId" }

        val circleMemberList = circleMemberRepository.findMemberByCircle(circleId, pageable)
        return circleMemberList
    }

    fun findMemberDetailsByCircle(circleMemberId: UUID, member: Member): CircleMemberDetailResponse {
        val memberDetailByCircle = circleMemberRepository.findMemberDetailsByCircle(circleMemberId)

        val isCircleMember = circleMemberRepository.existsByCircleIdAndMemberId(memberDetailByCircle.circleId, member.id)
        require(isCircleMember) { "모임에 가입된 멤버만 조회 가능합니다. circleId : ${memberDetailByCircle.circleId}" }

        return memberDetailByCircle
    }

    fun findCirclesByPagination(pageable: Pageable, request: CircleSearchRequest?)
            : Page<CirclePageResponse?> {
        // 회원/비회원 이용자에 따라 좋아요 필드 조회 여부, 이용자가 좋아요한 모임만 조회 여부 Repository 에서 구현
        if (request?.likeByMeOnly == true && request.memberId == null) {
            throw RuntimeException("회원 ID가 존재하지 않습니다.(내가 좋아요한 모임 조회)")
        }
        return circleRepository.findCirclesByPagination(pageable, request)
    }

}