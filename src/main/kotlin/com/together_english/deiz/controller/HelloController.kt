package com.together_english.deiz.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/")
class HelloController {


    @Operation(hidden = true)
    @GetMapping
    fun hello(): String {
        return "hello, world"
    }
}