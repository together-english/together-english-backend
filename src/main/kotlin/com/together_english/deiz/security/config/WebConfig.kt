package com.together_english.deiz.security.config

import com.together_english.deiz.common.OctetStreamReadMsgConverter
import com.together_english.deiz.security.util.CustomUserResolver
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
        private val octetStreamReadMsgConverter: OctetStreamReadMsgConverter
): WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CustomUserResolver())
    }
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(octetStreamReadMsgConverter)
    }
}