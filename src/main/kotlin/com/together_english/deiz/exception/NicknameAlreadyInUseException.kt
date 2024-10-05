package com.together_english.deiz.exception

class NicknameAlreadyInUseException(
        override val message: String = "이미 사용중인 닉네임입니다."
) : RuntimeException(message)