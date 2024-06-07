package com.together_english.deiz.data.entity

import com.together_english.deiz.common.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "members")
class Member(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        name: String,
        @Column(nullable = false, unique = true)
        val email: String,
        password: String
): BaseEntity() {
    var password: String = password
        private set
    var name: String = name
        private set
}