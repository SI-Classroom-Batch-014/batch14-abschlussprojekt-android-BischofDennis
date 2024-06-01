package com.example.glucoflow.dataRoom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Meal(
    @PrimaryKey
    val kohlenhydrate: String,
    val kalorien: String,
    val dateTime: String
)