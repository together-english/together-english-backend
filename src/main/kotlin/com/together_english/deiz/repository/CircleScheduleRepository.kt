package com.together_english.deiz.repository

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleSchedule
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CircleScheduleRepository : JpaRepository<CircleSchedule, UUID> {
    fun deleteAllByCircle(circle: Circle)
}