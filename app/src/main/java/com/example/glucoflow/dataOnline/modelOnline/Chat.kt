package com.example.glucoflow.dataOnline.modelOnline

data class Chat(
    // @DocumentId
    val chatId: String = "",
    val messages: MutableList<Message> = mutableListOf()
)
