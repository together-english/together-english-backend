package com.together_english.deiz.exception

class NotExistException(
    item: String,
    override val message: String = "해당 $item 가 존재하지 않습니다."
): RuntimeException(message)