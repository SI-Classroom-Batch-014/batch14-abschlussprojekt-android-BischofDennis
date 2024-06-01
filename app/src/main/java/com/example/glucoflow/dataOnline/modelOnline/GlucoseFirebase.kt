package com.example.glucoflow.dataOnline.modelOnline

data class GlucoseFirebase (
    //@DocumentId
    val id: Long = 0,
    val glucosevalue: String,
    val dateTime: String,
    val carbon: String
)