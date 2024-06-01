package com.example.glucoflow.dataRoom

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.glucoflow.dataRoom.model.Glucose
import com.example.glucoflow.dataRoom.model.Meal
import com.example.glucoflow.dataRoom.model.MyCalendar
import kotlin.Exception

const val TAG = "AppRepository"
class AppRepository(private val database: GlucoseDatabase,
                    private val databaseCalendar: CalendarDatabase,
                    private val databaseMeal: MealDatabase) {

    val glucoseList: LiveData<List<Glucose>> = database.glucoseDatabaseDao.getALLData()
    val calendarList: LiveData<List<MyCalendar>> = databaseCalendar.calendarDatabaseDao.getALLData()



    fun upsertGlucose(glucose: Glucose) {
        database.glucoseDatabaseDao.upsertGlucose(glucose)
    }

    fun deleteGlucose(glucose: Glucose){
        database.glucoseDatabaseDao.deleteGlucose(glucose)
    }

    suspend fun searchGlucose(searchTerm: String): List<Glucose> {
        return database.glucoseDatabaseDao.searchGlucose(searchTerm)
    }

    suspend fun searchGlucoseAll(): List<Glucose> {
        return database.glucoseDatabaseDao.searchGlucoseAll()
    }

    suspend fun searchMyCalendarAll(): List<MyCalendar> {
        return databaseCalendar.calendarDatabaseDao.searchCalendarAll()
    }

    suspend fun insert(glucose: Glucose) {
        try {
            database.glucoseDatabaseDao.insert(glucose)
        } catch (e: Exception) {
            Log.d(TAG,"Failed to insert into Database: $e")
        }
    }

    suspend fun insertCalendar(calendar: MyCalendar){
        try {
            databaseCalendar.calendarDatabaseDao.insert(calendar)
        } catch (e: Exception) {
            Log.d(TAG,"Failed to insert into Database: $e")
        }
    }


    //MVVM zum filtern
    suspend fun searchMealAll(): List<Meal> {
        return databaseMeal.mealDatabaseDao.searchMealAll()
    }

    suspend fun saveKhKcal(meal: Meal){
        try {
            databaseMeal.mealDatabaseDao.insert(meal)
        } catch (e: Exception) {
            Log.d(TAG,"Failed to insert into Database: $e")
        }
    }
}