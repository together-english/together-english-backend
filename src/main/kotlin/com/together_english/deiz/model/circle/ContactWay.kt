package com.together_english.deiz.model.circle

enum class ContactWay(val displayName: String, val description: String) {
    KAKAO_OPEN_CHAT("카카오톡 오픈채팅", "카카오톡 오픈채팅을 통해 연락합니다."),
    EMAIL("이메일", "이메일을 통해 연락합니다."),
    GOOGLE_FORM("구글 폼", "구글 폼을 통해 정보를 제출하고 연락합니다.")
}