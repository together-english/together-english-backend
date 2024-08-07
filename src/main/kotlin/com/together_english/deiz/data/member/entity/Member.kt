package com.together_english.deiz.data.member.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.together_english.deiz.common.base.BaseEntity
import com.together_english.deiz.data.member.Role
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "members")
class Member(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        name: String,
        nickname: String,
        @Column(nullable = false, unique = true)
        val email: String,
        hashedPassword: String,
        profile: String?
): BaseEntity(), UserDetails {
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
}