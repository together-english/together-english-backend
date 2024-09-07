package com.together_english.deiz.model.common

enum class EnglishLevel(val displayName: String, val description: String) {
    BEGINNER("Beginner", "기초적인 영어 실력. 기본적인 문장과 표현을 이해할 수 있음."),
    INTERMEDIATE("Intermediate", "중간 수준의 영어 실력. 일상적인 상황에서 영어로 의사소통이 가능함."),
    ADVANCED("Advanced", "고급 영어 실력. 다양한 주제에 대해 복잡한 문장으로 영어를 사용할 수 있음."),
    PROFICIENT("Proficient", "숙련된 영어 실력. 전문적인 환경에서도 영어로 자유롭게 의사소통 가능."),
    NATIVE("Native", "원어민 수준의 영어 실력.")
}