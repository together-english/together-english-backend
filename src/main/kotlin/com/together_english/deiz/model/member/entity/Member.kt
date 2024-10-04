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
        profile: String? = null,
        gender: Gender = Gender.NO,
        age: Int = 0,
        isTermsAgreed: Boolean,
        isPrivacyAgreed: Boolean,
        isMarketingAgreed: Boolean = false
): BaseEntity(), UserDetails {

    @Column(nullable = false, unique = true, length = 64)
    val email: String = email

    @Column(length = 64)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var hashedPassword: String = hashedPassword
        private set

    @Column(nullable = false, length = 64)
    var name: String = name
        private set

    @Column(unique = true, length = 64)
    var nickname: String = nickname
        private set

    @Column(length = 32)
    var phone: String? = null
        private set

    var profile: String? = profile
        private set

    var profileOriginName: String? = null
        private set

    var age: Int = age
        private set

    val isTermsAgreed = isTermsAgreed

    val isPrivacyAgreed = isPrivacyAgreed

    var isMarketingAgreed = isMarketingAgreed

    @Column(length = 4)
    @Enumerated(EnumType.STRING)
    var gender = gender
        private set

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "leader")
    val mutableCircles: MutableList<Circle> = mutableListOf()

    var valid: Boolean = true
        private set

    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
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

    fun updateProfile(updatedProfile: String, originFileName: String) {
        this.profile = updatedProfile
        this.profileOriginName = originFileName
    }
}