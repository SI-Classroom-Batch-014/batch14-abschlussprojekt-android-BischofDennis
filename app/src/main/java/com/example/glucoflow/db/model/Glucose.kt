package com.example.glucoflow.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Glucose(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val glucosevalue: String,
    val dateTime: String

)
