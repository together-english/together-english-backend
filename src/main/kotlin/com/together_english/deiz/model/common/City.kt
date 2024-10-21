package com.together_english.deiz.model.common

enum class City(private val displayName: String) {
    GANGNAM("강남"),
    SINCHON("신촌"),
    HONGDAE("홍대"),
    KONDAE("건대"),
    SINRIM("신림"),
    SUWON("수원"),
    PANGYO("판교"),
    INCHEON("인천"),
    DAEJEON("대전"),
    DAEGU("대구"),
    ULSAN("울산"),
    CHANGWON("창원"),
    BUSAN("부산"),
    ETC("기타"),
    ONLINE("온라인");

    override fun toString(): String {
        return name
    }
}