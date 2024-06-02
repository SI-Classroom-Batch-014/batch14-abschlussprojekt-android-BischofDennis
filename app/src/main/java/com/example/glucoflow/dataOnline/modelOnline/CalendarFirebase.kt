package com.example.glucoflow.dataOnline.modelOnline

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

data class CalendarFirebase(
    @DocumentId
    val id: String = "",
    val title: String,
    val date: String,
    val time: String,
    val haufigkeit: String? = null
)
