package com.together_english.deiz.chat.dto

data class UserChatRoomsInfo(
    var senderId: Long? = null,
    var senderName: String? = null,
    var receiverId: Long? = null,
    var receiverName: String? = null,
)