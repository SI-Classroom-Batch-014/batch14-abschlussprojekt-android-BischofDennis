package com.example.glucoflow.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey(autoGenerate = true)

    val name : String,
    val password : String

)
