package com.together_english.deiz.exception

class JWTException(
        override var message:String = "JWT Exception",
        ) : RuntimeException(message)