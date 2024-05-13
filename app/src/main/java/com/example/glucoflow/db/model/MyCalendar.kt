package com.example.glucoflow.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyCalendar (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val date: String,
    val time: String
)