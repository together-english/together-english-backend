package com.together_english.deiz.service

import com.together_english.deiz.exception.AlreadyExistException
import com.together_english.deiz.exception.NotExistException
import com.together_english.deiz.exception.UnAuthorizedAccessException
import com.together_english.deiz.model.circle.FavoriteCircle
import com.together_english.deiz.model.circle.dto.*
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.CircleRepository
import com.together_english.deiz.repository.CircleScheduleRepository
import com.together_english.deiz.repository.FavoriteCircleRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class CircleService(
    private val circleRepository: CircleRepository,
    private val s3ImageUploadService: S3ImageUploadService,
    private val circleScheduleRepository: CircleScheduleRepository,
    private val favoriteCircleRepository: FavoriteCircleRepository
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
        val existFavoriteCircle = favoriteCircleRepository.findByCircleAndMember(circle, member)
        if (existFavoriteCircle != null) {
            throw AlreadyExistException("모임 좋아요 기록(circle id: ${existFavoriteCircle.circle.id.toString()})")
        }

        val favoriteCircle = FavoriteCircle(circle, member)
        favoriteCircleRepository.save(favoriteCircle)
    }

    @Transactional
    fun removeFavoriteToCircle(id: UUID, member: Member) {
        val circle = circleRepository.findById(id).orElseThrow { NotExistException("circle id: $id") }
        val favoriteCircle = favoriteCircleRepository.findByCircleAndMember(circle, member)
            ?: throw NotExistException("모임 좋아요 기록(circle id: ${id.toString()})")

        favoriteCircleRepository.delete(favoriteCircle)
    }

    @Transactional(readOnly = true)
    fun getCircleDetail(id: UUID) : CircleDetailResponse {
        val circle = circleRepository.findById(id).orElseThrow { NotExistException("circle id: $id") }
        return CircleDetailResponse.fromEntity(circle)
    }

    fun findCirclesByPagination(pageable: Pageable, request: CircleSearchRequest?)
            : Page<CirclePageResponse?> {
        return circleRepository.findCirclesByPagination(pageable, request)
    }
}