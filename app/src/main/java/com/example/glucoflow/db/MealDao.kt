package com.example.glucoflow.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.glucoflow.db.model.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertMeal(meal: Meal)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertALL(meal: List<Meal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Meal)

    @Query("SELECT * from Meal")
    fun getALLData(): LiveData<List<Meal>>

    @Query("DELETE from Meal")
    fun deleteALL()

    @Delete
    fun deleteMeal(meal: Meal)

    @Query("SELECT * from Meal")
    suspend fun searchMealAll() : List<Meal>
}