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
        @Column(nullable = false, unique = true)
        val email: String,
        password: String
): BaseEntity(), UserDetails {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private var password: String = password

    @Column(nullable = false)
    var name: String = name
        private set

    var nickname: String = name
        private set

    var phone: String? = null
        private set

    var profile: String? = null
        private set

    var valid: Boolean = true
        private set

    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    var roles: MutableList<Role> = mutableListOf(Role.USER)
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    override fun getPassword(): String {
        return this.password;
    }

    override fun getUsername(): String {
        return this.email
    }
}