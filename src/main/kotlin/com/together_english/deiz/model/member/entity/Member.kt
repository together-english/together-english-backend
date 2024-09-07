package com.together_english.deiz.model.member.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.together_english.deiz.common.base.BaseEntity
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.member.Gender
import com.together_english.deiz.model.member.Role
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
class Member(
        name: String,
        nickname: String,
        email: String,
        hashedPassword: String,
        profile: String?,
        gender: Gender = Gender.NO,
        age: Int = 0
): BaseEntity(), UserDetails {

    @Column(nullable = false, unique = true)
    val email: String = email

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var hashedPassword: String = hashedPassword
        private set

    @Column(nullable = false)
    var name: String = name
        private set

    var nickname: String = nickname
        private set

    var phone: String? = null
        private set

    var profile: String? = profile
        private set

    var age: Int = age
        private set

    var gender = gender
        private set

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "leader")
    val mutableCircles: MutableList<Circle> = mutableListOf()

    var valid: Boolean = true
        private set

    @ElementCollection(targetClass = Role::class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    var roles: MutableList<Role> = mutableListOf(Role.USER)
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    override fun getPassword(): String {
        return this.hashedPassword
    }

    override fun getUsername(): String {
        return this.email
    }
}