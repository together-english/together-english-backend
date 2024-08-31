package com.together_english.deiz.exception

class UserAlreadyExistException(
        override val message: String = "해당 유저가 이미 존재합니다."
) : RuntimeException(message)