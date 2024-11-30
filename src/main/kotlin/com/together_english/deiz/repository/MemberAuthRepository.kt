package com.together_english.deiz.repository

import com.together_english.deiz.model.member.entity.MemberAuth
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberAuthRepository : JpaRepository<MemberAuth, String> {
    fun findByAuthKey(authKey: UUID): Optional<MemberAuth>
}