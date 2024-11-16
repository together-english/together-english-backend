package com.together_english.deiz.exception

class UnAuthorizedAccessException(
    override val message: String = "해당 기능을 수행할 권한이 없습니다.",
) : RuntimeException(message)