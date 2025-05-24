package com.together_english.deiz.service

import com.together_english.deiz.exception.AlreadyExistException
import com.together_english.deiz.exception.NotExistException
import com.together_english.deiz.exception.UnAuthorizedAccessException
import com.together_english.deiz.model.circle.CircleJoinStatus.*
import com.together_english.deiz.model.circle.CircleMember
import com.together_english.deiz.model.circle.FavoriteCircle
import com.together_english.deiz.model.circle.dto.*
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.*
import com.together_english.deiz.repository.custom.CircleQueryDslRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class CircleService(
    private val circleRepository: CircleRepository,
    private val circleQueryDslRepository: CircleQueryDslRepository,
    private val circleJoinRepository: CircleJoinRepository,
    private val circleMemberRepository: CircleMemberRepository,
    private val s3ImageUploadService: S3ImageUploadService,
    private val circleScheduleRepository: CircleScheduleRepository,
    private val favoriteCircleRepository: FavoriteCircleRepository,
    private val notificationService: NotificationService
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
        circleMemberToSave.updateRole(CircleMember.CircleRole.LEADER)
        circleMemberRepository.save(circleMemberToSave)

        return circle.id.toString()
    }

    @Transactional
    fun updateCircleWithSchedule(
        id: UUID,
        thumbnailFile: MultipartFile?,
        request: CircleUpdateRequest,
        member: Member) {
        val circle = circleRepository.findById(id).orElseThrow {
            NotExistException("circle id: $request.id")
        }
        if (circle.leader.id != member.id) {
            throw UnAuthorizedAccessException()
        }
        thumbnailFile?.let {
            circle.thumbnailUrl?.let { thumbnailUrl ->
                s3ImageUploadService.deleteFileFromUrl(thumbnailUrl)
            }
            circle.updateThumbnail(s3ImageUploadService.uploadFile(it))
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
        circle.thumbnailUrl?.let { thumbnailUrl -> s3ImageUploadService.deleteFileFromUrl(thumbnailUrl) }
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

        circle.increaseLikeCount()

        val favoriteCircle = FavoriteCircle(circle, member)
        favoriteCircleRepository.save(favoriteCircle)
    }

    @Transactional
    fun removeFavoriteToCircle(id: UUID, member: Member) {
        val circle = circleRepository.findById(id).orElseThrow { NotExistException("circle id: $id") }
        val favoriteCircle = favoriteCircleRepository.findByCircleAndMember(circle, member)
            ?: throw NotExistException("모임 좋아요 기록(circle id: ${id})")

        circle.decreaseLikeCount()
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

        val circleMember = circleMemberRepository.findByCircleAndMemberAndStatusIsNot(
            circle,
            member,
            CircleMember.CircleMemberStatus.BANNED
        )
        require(circleMember.isEmpty) { "이미 모임에 가입된 회원입니다. circle id : ${request.circleId}" }

        val circleJoinRequest =
            circleJoinRepository.findByCircleIdAndMemberIdAndStatusIsNot(circle.id, member.id, LEAVED)

        if (circleJoinRequest != null) {
            when (circleJoinRequest.status) {
                WAITING -> throw RuntimeException("이미 요청을 보냈습니다.")
                ACCEPTED -> throw RuntimeException("이미 가입 승인된 회원입니다.")
                REJECTED -> throw RuntimeException("가입요청이 거절된 회원입니다.")
                BANISHED -> throw RuntimeException("가입이 제한된 회원입니다.")
                else -> throw RuntimeException("가입이 제한된 회원입니다.")
            }
        }

        notificationService.publishNotification(circle.leader.id,
            "${member.nickname} 로 부터 ${circle.title}의 " + "영어모임 가입 요청이 도착했습니다.")

        val circleJoinRequestToSave = request.toEntity(circle, member)
        circleJoinRepository.save(circleJoinRequestToSave)
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
        require(circleJoinRequest.status == WAITING) { "모임 가입요청이 대기상태인 경우만 수정 가능합니다. 현재 상태: ${circleJoinRequest.status}" }

        circleJoinRequest.updateMessage(request)
        return circleJoinRequest.id.toString()
    }

    @Transactional
    fun approveCircleJoinRequest(circleJoinRequestId: UUID, member: Member): String {
        val circleJoinRequest = circleJoinRepository.findById(circleJoinRequestId)
            .orElseThrow { NotExistException("circle join request id: $circleJoinRequestId") }

        val circleMember = circleMemberRepository.findByCircleAndMemberAndStatusIsNot(
            circleJoinRequest.circle,
            member,
            CircleMember.CircleMemberStatus.BANNED
        )
            .orElseThrow { Exception("해당 모임의 팀원이 아닙니다. 가입요청 승인이 불가능합니다.") }

        require(circleMember.role == CircleMember.CircleRole.LEADER) { "모임 리더권한 유저만 모임 가입요청 승인이 가능합니다." }
        require(circleJoinRequest.status == WAITING) { "모임 가입요청이 대기상태인 경우만 승인 가능합니다. 현재 상태: ${circleJoinRequest.status}" }

        val circleMemberToSave = circleJoinRequest.toCircleMemberEntity()
        circleMemberRepository.save(circleMemberToSave)
        circleJoinRequest.updateStatus(ACCEPTED)

        notificationService.publishNotification(circleJoinRequest.member.id, "${circleMember.circle.title} 해당 모임에 가입이 승인 되었습니다.")

        return circleMemberToSave.id.toString()
    }

    @Transactional
    fun rejectCircleJoinRequest(circleJoinRequestId: UUID, member: Member) {
        val circleJoinRequest = circleJoinRepository.findById(circleJoinRequestId)
            .orElseThrow { NotExistException("circle join request id: $circleJoinRequestId") }

        val circleMember = circleMemberRepository.findByCircleAndMemberAndStatusIsNot(
            circleJoinRequest.circle,
            member,
            CircleMember.CircleMemberStatus.BANNED
        )
            .orElseThrow { Exception("해당 모임의 팀원이 아닙니다. 가입요청 거절이 불가능합니다.") }

        require(circleMember.role == CircleMember.CircleRole.LEADER) { "모임 리더권한 유저만 모임 가입요청 거절이 가능합니다." }
        require(circleJoinRequest.status == WAITING) { "모임 가입요청이 대기상태인 경우만 거절 가능합니다. 현재 상태: ${circleJoinRequest.status}" }
        circleJoinRequest.updateStatus(REJECTED)
        notificationService.publishNotification(circleJoinRequest.member.id, "${circleMember.circle.title} 해당 모임에 가입이 거절 되었습니다.")
        
    }

    fun findMemberByCircleList(circleId: UUID, pageable: Pageable): Page<CircleMemberPageResponse?> {
        return circleMemberRepository.findMemberByCircle(circleId, pageable)
    }

    fun findMemberDetailsByCircle(circleMemberId: UUID, member: Member): CircleMemberDetailResponse {
        val memberDetailByCircle = circleMemberRepository.findMemberDetailsByCircle(circleMemberId)

        val isCircleMember =
            circleMemberRepository.existsByCircleIdAndMemberId(memberDetailByCircle.circleId, member.id)
        require(isCircleMember) { "모임에 가입된 멤버만 조회 가능합니다. circleId : ${memberDetailByCircle.circleId}" }

        return memberDetailByCircle
    }

    @Transactional
    fun leaveCircleMember(circleMemberId: UUID, member: Member) {
        val circleMember = circleMemberRepository.findById(circleMemberId)
            .orElseThrow { NotExistException("circle member id : $circleMemberId") }
        require(circleMember.member.id == member.id) { "자신이 가입한 모임 정보가 아닙니다. 확인 후 다시 시도해주세요" }

        val circleJoinRequest =
            circleJoinRepository.findByCircleIdAndMemberIdAndStatusIs(
                circleMember.circle.id,
                circleMember.member.id,
                ACCEPTED
            )
        when (circleJoinRequest.status) {
            LEAVED -> throw RuntimeException("이미 탈퇴 완료된 모임입니다.")
            ACCEPTED -> circleJoinRequest.updateStatus(LEAVED)
            else -> throw RuntimeException("에러: 가입되지 않은 모임 탈퇴 시도 circleMemberId : $circleMemberId.")
        }

        circleMemberRepository.deleteById(circleMemberId)
        notificationService.publishNotification(circleMember.circle.leader.id,
            "${circleMember.circle.title}모임에서 ${member.nickname} 님이 탈퇴하였습니다.")

    }

    @Transactional
    fun banishedCircleMember(circleMemberId: UUID, member: Member) {
        val circleMember = circleMemberRepository.findById(circleMemberId)
            .orElseThrow { Exception("해당 모임의 팀원 정보가 없습니다. circleMemberId : $circleMemberId") }

        val circleLeader = circleMemberRepository.findByCircleAndMemberAndStatusIsNot(
            circleMember.circle,
            member,
            CircleMember.CircleMemberStatus.BANNED
        )
            .orElseThrow { Exception("로그인한 사용자의 모임 가입 정보가 존재하지 않습니다. circleId : ${circleMember.circle.id}") }
        require(circleLeader.role == CircleMember.CircleRole.LEADER) { "모임 리더권한 유저만 모임 추방이 가능합니다." }
        require(circleMember.id != circleLeader.id) { "자신을 추방할 수 없습니다. 확인 후 다시 시도해주세요." }
        require(circleMember.status != CircleMember.CircleMemberStatus.BANNED) { "이미 추방이 완료된 인원입니다." }

        val circleJoinRequest =
            circleJoinRepository.findByCircleIdAndMemberIdAndStatusIs(
                circleMember.circle.id,
                circleMember.member.id,
                ACCEPTED
            )
        when (circleJoinRequest.status) {
            LEAVED -> throw RuntimeException("이미 탈퇴 완료된 모임입니다.")
            ACCEPTED -> circleJoinRequest.updateStatus(BANISHED)
            else -> throw RuntimeException("에러: 가입되지 않은 모임 탈퇴 시도 circleMemberId : $circleMemberId.")
        }

        circleMember.updateStatus(CircleMember.CircleMemberStatus.BANNED)
        notificationService.publishNotification(circleJoinRequest.member.id, "${circleMember.circle.title} 모임에서 강퇴되었습니다.")

    }

    fun findCirclesByPagination(pageable: Pageable, request: CircleSearchRequest?)
            : Page<CirclePageResponse> {
        return if (request?.memberId != null) {
            circleQueryDslRepository.searchPageForMember(request = request, pageable = pageable)
        } else {
            circleQueryDslRepository.searchPageForAnonymous(request = request, pageable = pageable)
        }
    }

}