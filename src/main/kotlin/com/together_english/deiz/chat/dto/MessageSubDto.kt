package com.together_english.deiz.chat.dto

data class MessageSubDto(
    var userId: Long? = null,
    var partnerId: Long? = null,
    var chatMessageDto: ChatMessageDto? = null,
    var list: List<ChatRoomListGetResponse>? = null,
    var partnerList: List<ChatRoomListGetResponse>? = null
) {

}