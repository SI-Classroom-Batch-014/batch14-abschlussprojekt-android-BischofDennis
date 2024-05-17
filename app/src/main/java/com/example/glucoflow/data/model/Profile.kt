package com.example.glucoflow.data.model

import com.google.firebase.firestore.DocumentId

data class Profile(
    @DocumentId
    val userId: String = "",
    val username: String = ""
)
