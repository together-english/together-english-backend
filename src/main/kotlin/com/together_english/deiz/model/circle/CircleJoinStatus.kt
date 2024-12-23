package com.together_english.deiz.model.circle

enum class CircleJoinStatus(private val value: String) {
    REJECTED("reject"), ACCEPTED("accepted"), EXPIRED("expired"),
    WAITING("waiting"), LEAVED("leaved"), BANISHED("banished"),;
}