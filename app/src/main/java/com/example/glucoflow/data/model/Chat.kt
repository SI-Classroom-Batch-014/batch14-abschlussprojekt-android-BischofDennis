package com.example.glucoflow.data.model

data class Chat(
    // @DocumentId
    val chatId: String = "",
    val message: MutableList<Message> = mutableListOf()
)
