package com.together_english.deiz.model.circle

import java.io.Serializable
import java.util.UUID

data class FavoriteCircleId(
    val circle: UUID,
    val member: UUID
) : Serializable {
    constructor() : this(UUID(0, 0), UUID(0, 0))
}