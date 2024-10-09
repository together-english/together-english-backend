package com.together_english.deiz.model.circle

import com.together_english.deiz.common.base.BaseEntity
import com.together_english.deiz.model.common.DayOfWeek
import jakarta.persistence.*

@Entity
class CircleSchedule(
    circle: Circle,
    dayOfWeek: DayOfWeek,
    startTime: String,
    endTime: String
) : BaseEntity() {
    @JoinColumn(name = "circle_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    val circle: Circle = circle
    @Enumerated(value = EnumType.STRING)
    var dayOfWeek = dayOfWeek
    var startTime = startTime
    var endTime = endTime
}