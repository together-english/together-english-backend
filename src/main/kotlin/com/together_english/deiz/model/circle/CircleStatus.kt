package com.together_english.deiz.model.circle

enum class CircleStatus(val displayName: String, val description: String) {
    RECRUITING("모집중", "현재 새로운 멤버를 모집하고 있습니다."),
    CLOSED("모집 마감", "더 이상 멤버를 모집하지 않습니다."),
    INACTIVE("활동 중지", "활동이 일시 중지되었습니다."),
    COMPLETED("활동 완료", "모든 활동이 완료되었습니다."),
    REPORTED("신고됨", "이 서클은 신고된 상태입니다."),
    DELETED("삭제됨", "이 서클은 삭제되었습니다.")
}