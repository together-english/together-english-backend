package com.together_english.deiz.model.circle

import com.together_english.deiz.common.base.BaseTimeEntity
import com.together_english.deiz.model.common.DayOfWeek
import jakarta.persistence.*
import java.util.*

@Entity
class CircleSchedule(
    circle: Circle,
    dayOfWeek: DayOfWeek,
    startTime: String,
    endTime: String
) : BaseTimeEntity() {
    @Id
    val id: UUID = UUID.randomUUID()
    @JoinColumn(name = "circle_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    val circle: Circle = circle
    @Enumerated(value = EnumType.STRING)
    var dayOfWeek = dayOfWeek
    var startTime = startTime
    var endTime = endTime
}