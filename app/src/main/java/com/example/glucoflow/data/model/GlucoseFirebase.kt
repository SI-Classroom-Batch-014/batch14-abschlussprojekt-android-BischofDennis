package com.example.glucoflow.data.model

import com.google.firebase.firestore.DocumentId

data class GlucoseFirebase (
    //@DocumentId
    val id: Long = 0,
    val glucosevalue: String,
    val dateTime: String,
    val carbon: String
)