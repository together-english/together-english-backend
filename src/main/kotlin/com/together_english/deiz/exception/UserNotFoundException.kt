package com.together_english.deiz.exception

class UserNotFoundException(
        override val message: String = "해당 유저가 존재하지 않습니다."
) : RuntimeException(message) {
}