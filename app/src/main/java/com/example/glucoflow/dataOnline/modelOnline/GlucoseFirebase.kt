package com.example.glucoflow.dataOnline.modelOnline

import com.google.firebase.firestore.DocumentId

data class GlucoseFirebase (
    @DocumentId
    val id: String = "",
    val glucosevalue: String,
    val dateTime: String,
    val carbon: String
)