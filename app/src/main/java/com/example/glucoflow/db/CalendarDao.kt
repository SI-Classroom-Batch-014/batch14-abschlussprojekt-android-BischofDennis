package com.example.glucoflow.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.glucoflow.db.model.Glucose
import com.example.glucoflow.db.model.MyCalendar

@Dao
interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCalendar(calendar: MyCalendar)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertALL(calendar: List<MyCalendar>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calendar: MyCalendar)

    @Query("SELECT * from MyCalendar")
    fun getALLData(): LiveData<List<MyCalendar>>

    @Query("DELETE from MyCalendar")
    fun deleteALL()

    @Delete
    fun deleteCalendar(calendar: MyCalendar)


    @Query("SELECT * from MyCalendar")
    suspend fun searchCalendarAll() : List<MyCalendar>
}