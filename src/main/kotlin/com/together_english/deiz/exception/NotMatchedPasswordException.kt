package com.together_english.deiz.exception

class NotMatchedPasswordException(
        override val message: String = "패스워드가 일치하지 않습니다."
) : RuntimeException(message)