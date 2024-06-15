package com.together_english.deiz.data.member.repository

import com.together_english.deiz.data.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Long>{
    fun findByEmail(email: String): Optional<Member>
}