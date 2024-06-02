package com.example.glucoflow.dataOnline.modelOnline

import com.google.firebase.firestore.DocumentId

data class Profile(
    @DocumentId
    val userId: String = "",
    val username: String = ""
)
