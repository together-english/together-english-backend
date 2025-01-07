package com.together_english.deiz.model.circle

import com.together_english.deiz.common.base.BaseTimeEntity
import com.together_english.deiz.model.comment.CircleComment
import com.together_english.deiz.model.circle.dto.CircleUpdateRequest
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.entity.Member
import jakarta.persistence.*
import java.util.*

@Entity
class Circle(
        leader: Member,
        title: String,
        englishLevel: EnglishLevel,
        city: City,
        thumbnailUrl: String? = null,
        introduction: String,
        address: String? = null,
        capacity: Int,
        attendMode: AttendMode,
        contactWay: String?,
        onlineUrl: String? = null,
): BaseTimeEntity(){

    @Id
    val id: UUID = UUID.randomUUID()

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "leader_id", nullable = false)
    val leader:Member = leader

    @Column(nullable = false, length = 255)
    var title: String = title
        protected set

    @Column(nullable = false, length = 32)
    @Enumerated(value = EnumType.STRING)
    var englishLevel: EnglishLevel = englishLevel
        protected set

    @Column(nullable = false, length = 32)
    @Enumerated(value = EnumType.STRING)
    var city: City = city
        protected set

    @Column(length = 255)
    var thumbnailUrl: String? = thumbnailUrl
        protected set

    @Column(length = 3000)
    var introduction: String = introduction
        protected set

    @Column(length = 255)
    var address: String? = address
        protected set

    @Column(nullable = false)
    var capacity: Int = capacity
        protected set

    @Column(nullable = false, length = 32)
    @Enumerated(value = EnumType.STRING)
    var circleStatus: CircleStatus = CircleStatus.RECRUITING
        protected set

    @Column(nullable = false, length = 32)
    @Enumerated(value = EnumType.STRING)
    var attendMode:AttendMode = attendMode
        protected set

    @Column(length = 255)
    var onlineUrl:String? = onlineUrl

    @Column(nullable = true, length = 32)
    var contactWay = contactWay
        protected set

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "circle")
    var circleSchedules: MutableList<CircleSchedule> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "circle")
    var favoriteCircle: MutableList<FavoriteCircle> = mutableListOf()

    var totalView: Int = 0
        protected set

    var totalLike: Int = 0
        protected set

    var weekView: Int = 0
        protected set

    var valid: Boolean = true
        protected set

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "circle")
    var circleComments: MutableList<CircleComment> = mutableListOf()

    fun update(request: CircleUpdateRequest) {
        this.title = request.title
        this.englishLevel = request.englishLevel
        this.city = request.city
        this.introduction = request.introduction
        this.address = request.address
        this.capacity = request.capacity
        this.attendMode = request.attendMode
        this.contactWay = request.contactWay
        this.onlineUrl = request.onlineUrl
    }

    fun updateThumbnail(thumbnailUrl: String?) {
        this.thumbnailUrl = thumbnailUrl
    }

    fun delete() {
        this.valid = false
    }

    fun increaseLikeCount() {
        this.totalLike += 1
    }

    fun decreaseLikeCount() {
        this.totalLike -= 1
    }
    fun isWrittenBy(member: Member): Boolean {
        return this.leader.id == member.id
    }
}