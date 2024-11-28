package com.together_english.deiz.model.member.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class MemberAuth(
    email: String,
    authKey: UUID
) {
    @Id
    @Column(nullable = false, unique = true, length = 64)
    val email: String = email

    @Column(nullable = false)
    val authKey: UUID = authKey
}