package com.example.glucoflow.dataOnline.modelOnline

import com.google.firebase.firestore.DocumentId

data class MealFirebase (
    @DocumentId
    val id : String = "",
    val kohlenhydrate: String,
    val kalorien: String,
    val dateTime: String
)