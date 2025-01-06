package com.together_english.deiz.service

import com.mailjet.client.ClientOptions
import com.mailjet.client.MailjetClient
import com.mailjet.client.MailjetRequest
import com.mailjet.client.MailjetResponse
import com.mailjet.client.resource.Emailv31
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MailApiService(
    @Value("\${auth.mail-api.key}") private val API_KEY: String,
    @Value("\${auth.mail-api.secret-key}") private val API_SECRET_KEY: String,
) {
    fun callSendPasswordResetEmail(email: String, userName: String, sendURL: String): MailjetResponse {
        val clientOptions = ClientOptions.builder()
            .apiKey(API_KEY)
            .apiSecretKey(API_SECRET_KEY)
            .build()
        val client = MailjetClient(clientOptions)

        val request = MailjetRequest(Emailv31.resource)
            .property(
                Emailv31.MESSAGES,
                JSONArray()
                    .put(
                        JSONObject()
                            .put(
                                Emailv31.Message.FROM, JSONObject()
                                    .put("Email", "together.english.manager@gmail.com")
                                    .put("Name", "together-english")
                            )
                            .put(
                                Emailv31.Message.TO, JSONArray()
                                    .put(
                                        JSONObject()
                                            .put("Email", email)
                                            .put("Name", userName)
                                    )
                            )
                            .put(Emailv31.Message.SUBJECT, "[together-english] 비밀번호 재설정 메일입니다.")
                            .put(Emailv31.Message.TEXTPART, "TEXTPART 위치요!")
                            .put(
                                Emailv31.Message.HTMLPART,
                                "<h3>안녕하세요 비밀번호 재설정 메일입니다.<br />" +
                                        "<a href=\"$sendURL\">비밀번호 재설정 하기</a></h3>"
                            )
                    )
            )

        val response: MailjetResponse = client.post(request)
        return response
    }
}