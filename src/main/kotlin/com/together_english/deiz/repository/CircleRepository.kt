package com.together_english.deiz.repository

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.repository.custom.CustomCircleRepository
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CircleRepository : JpaRepository<Circle, UUID>, CustomCircleRepository {
}