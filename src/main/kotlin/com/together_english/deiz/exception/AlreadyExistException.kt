package com.together_english.deiz.exception

class AlreadyExistException(
    item: String,
    override val message: String = "해당 $item 은 이미 존재합니다."
) : RuntimeException(message)