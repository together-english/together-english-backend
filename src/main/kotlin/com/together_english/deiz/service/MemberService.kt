package com.together_english.deiz.service

import com.together_english.deiz.exception.NicknameAlreadyInUseException
import com.together_english.deiz.exception.UserNotFoundException
import com.together_english.deiz.model.member.dto.MyCreatedCirclePageResponse
import com.together_english.deiz.model.member.dto.MyJoinedCirclePageResponse
import com.together_english.deiz.model.member.dto.MyPageResponse
import com.together_english.deiz.model.member.dto.MyPageUpdateRequest
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.CircleMemberRepository
import com.together_english.deiz.repository.CircleRepository
import com.together_english.deiz.repository.MemberRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val s3ImageUploadService: S3ImageUploadService,
    private val passwordEncoder: PasswordEncoder,
    private val circleMemberRepository: CircleMemberRepository,
    private val circleRepository: CircleRepository,
) {

    @Transactional
    fun updateMemberProfile(
        file: MultipartFile,
        memberEmail: String
    ): String {
        var member = memberRepository.findByEmail(memberEmail)
            .orElseThrow { UserNotFoundException() }
        val profileUrl = s3ImageUploadService.uploadFile(file)
        member.updateProfile(profileUrl, file.originalFilename ?: "noNameFile")
        return profileUrl
    }

    @Transactional
    fun updateMyInfo(memberEmail: String, request: MyPageUpdateRequest) {
        var member = memberRepository.findByEmail(memberEmail)
            .orElseThrow { UserNotFoundException() }
        if (member.nickname != request.nickname && request.nickname != "") {
            if (memberRepository.findByNickname(request.nickname).isPresent) {
                throw NicknameAlreadyInUseException()
            }
        }
        member.updateMyInfo(request, passwordEncoder)
    }

    fun getMyInfo(memberEmail: String): MyPageResponse {
        var member = memberRepository.findByEmail(memberEmail)
            .orElseThrow { UserNotFoundException() }
        return member.toMyPageResponse()
    }

    fun getMyJoinedCircleList(member: Member, pageable: Pageable): Page<MyJoinedCirclePageResponse?> {
        val myJoinedCircleList = circleMemberRepository.findCircleByMember(member, pageable)
        return myJoinedCircleList
    }

    fun getMyCreatedCircleList(member: Member, pageable: Pageable): Page<MyCreatedCirclePageResponse?> {
        val myCreatedCircleList = circleRepository.findCreatedCirclesByPagination(member, pageable)
        return myCreatedCircleList
    }

}