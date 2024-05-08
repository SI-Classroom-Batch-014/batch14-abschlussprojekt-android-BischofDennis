package com.example.glucoflow.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.glucoflow.db.model.Glucose

@Dao
interface GlucoseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertGlucose(glucose: Glucose)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertALL(glucose: List<Glucose>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(glucose: Glucose)

    @Query("SELECT * from Glucose")
    fun getALLData(): LiveData<List<Glucose>>

    @Query("DELETE from Glucose")
    fun deleteALL()

    @Delete
    fun deleteGlucose(glucose: Glucose)

    @Query("SELECT * from Glucose WHERE instr(glucosevalue, :searchTerm) OR instr(dateTime, :searchTerm)")
    suspend fun searchGlucose(searchTerm: String) : List<Glucose>
}