package com.together_english.deiz.service

import com.together_english.deiz.model.circle.dto.CircleCreateRequest
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.CircleRepository
import com.together_english.deiz.repository.CircleScheduleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class CircleService(
        private val circleRepository: CircleRepository,
        private val s3ImageUploadService: S3ImageUploadService,
        private val circleScheduleRepository: CircleScheduleRepository
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
    fun updateCircleWithSchedule() {

    }

    @Transactional
    fun deleteCircleWithSchedule() {

    }

    fun findCirclePageForAnonymous() {

    }

    fun findCirclePageForMember() {

    }

    fun getCircleDetail() {

    }

    fun updateCircleFavorite() {

    }

}