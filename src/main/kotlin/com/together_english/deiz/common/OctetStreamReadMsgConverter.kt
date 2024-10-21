package com.together_english.deiz.common

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import java.lang.reflect.Type

@Component
class OctetStreamReadMsgConverter(
        objectMapper: ObjectMapper
) : AbstractJackson2HttpMessageConverter(objectMapper, MediaType.APPLICATION_OCTET_STREAM) {

    // 기존 application/octet-stream 타입을 쓰기로 다루는 메시지 컨버터가 이미 존재 (ByteArrayHttpMessageConverter)
    // 따라서 해당 컨버터는 쓰기 작업에는 이용하면 안됨
    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return false
    }

    override fun canWrite(@Nullable type: Type?, clazz: Class<*>, @Nullable mediaType: MediaType?): Boolean {
        return false
    }

    override fun canWrite(mediaType: MediaType?): Boolean {
        return false
    }
}