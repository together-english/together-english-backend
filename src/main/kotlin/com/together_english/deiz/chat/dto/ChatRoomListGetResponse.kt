package com.together_english.deiz.chat.dto

data class ChatRoomListGetResponse(
    var roomId: Long? = null,
    var userId: Long? = null,
    var userName: String? = null,
    var partnerId: Long? = null,
    var partnerName: String? = null,

    var lastMessage: String? = null,
) {
}