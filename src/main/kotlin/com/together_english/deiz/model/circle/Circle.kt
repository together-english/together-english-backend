package com.together_english.deiz.model.circle

import com.together_english.deiz.common.base.BaseEntity
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.entity.Member
import jakarta.persistence.*

@Entity
class Circle(
        leader: Member,
        name: String,
        englishLevel: EnglishLevel,
        city: City,
        thumbnailUrl: String? = null,
        introduction: String,
        address: String? = null,
        capacity: Int,
        attendMode: AttendMode,
        contactWay: ContactWay,
        onlineUrl: String? = null,
) : BaseEntity(){
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "leader_id", nullable = false)
    val leader:Member = leader

    @Column(nullable = false, length = 255)
    var name: String = name
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

    @Column(nullable = false, length = 32)
    @Enumerated(value = EnumType.STRING)
    var contactWay: ContactWay = contactWay
        protected set

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "circle")
    var circleSchedules: MutableList<CircleSchedule> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "circle")
    var favoriteCircle: MutableList<FavoriteCircle> = mutableListOf()

    var totalView: Int = 0
        protected set

    var weekView: Int = 0
        protected set

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "circle")
    var circleComments: MutableList<CircleComment> = mutableListOf()
}