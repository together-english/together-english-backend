package com.together_english.deiz.security.service

import com.together_english.deiz.exception.UserNotFoundException
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.MemberRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class MemberService(
        private val memberRepository: MemberRepository,
        private val s3ImageUploadService: S3ImageUploadService
) {

    @Transactional
    fun updateMemberProfile(
            file: MultipartFile,
            memberEmail: String
    ): String {
        var member = memberRepository.findByEmail(memberEmail)
                .orElseThrow { UserNotFoundException() }
        val profileUrl = s3ImageUploadService.uploadFile(file)
        member.updateProfile(profileUrl, file.originalFilename?: "noNameFile")
        return profileUrl
    }

}