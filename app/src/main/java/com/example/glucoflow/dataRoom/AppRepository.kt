package com.example.glucoflow.dataRoom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.glucoflow.dataOnline.modelOnline.GlucoseFirebase
import com.example.glucoflow.dataOnline.remote.MealApi
import com.example.glucoflow.dataRoom.model.Glucose
import com.example.glucoflow.dataRoom.model.Meal
import com.example.glucoflow.dataRoom.model.MyCalendar
import kotlin.Exception

const val TAG = "AppRepository"


/**
 * ➔ Das Repository bekommt ein Objekt vom Typ MealApi übergeben
 * ➔ Danach kann es mit Hilfe dieses Objektes die Anfrage an die API stellen
 */
//API-ROOM zusammengemacht
class AppRepository(private val mealAPI : MealApi,
                    private val database: GlucoseDatabase,
                    private val databaseCalendar: CalendarDatabase,
                    private val databaseMeal: MealDatabase) {

    val glucoseList: LiveData<List<Glucose>> = database.glucoseDatabaseDao.getALLData()
    val calendarList: LiveData<List<MyCalendar>> = databaseCalendar.calendarDatabaseDao.getALLData()
    //API_Repository
    var index = 0

    private var _meals = MutableLiveData<List<com.example.glucoflow.dataOnline.modelOnline.Meal>>()
    val meals : LiveData<List<com.example.glucoflow.dataOnline.modelOnline.Meal>>
        get() = _meals


    suspend fun getMealsBySearch(search: String){
        try {
            val result = mealAPI.retrofitService.getMealBySearch(search)
            _meals.value = result.meals.shuffled()
            //alternative _memes.postValue(result.memesList)
        }catch (e: Exception){
            Log.e(com.example.glucoflow.dataOnline.TAG, "Error loading Data from API: $e")
        }
    }


    suspend fun getRandomMeal(){
        try {
            val result = mealAPI.retrofitService.getRandomMeal()
            _meals.value = result.meals.shuffled()
            //alternative _memes.postValue(result.memesList)
        }catch (e: Exception){
            Log.e(com.example.glucoflow.dataOnline.TAG, "Error loading Data from API: $e")
        }
    }

    suspend fun getMealCategory(category: String){
        try {
            val result = mealAPI.retrofitService.getMealsByCategory(category)
            _meals.value = result.meals.shuffled()
            //alternative _memes.postValue(result.memesList)
        }catch (e: Exception){
            Log.e(com.example.glucoflow.dataOnline.TAG, "Error loading Data from API: $e")
        }
    }

    //der Category Call ist anders aufgebaut (3 Eigenschaften) als der normale Call
    suspend fun getMealbyId(id: Int): com.example.glucoflow.dataOnline.modelOnline.Meal {
        return try {

            mealAPI.retrofitService.getMealById(id).meals.first()
        }catch (e: Exception){
            Log.e(com.example.glucoflow.dataOnline.TAG, "Error loading Data from API: $e")
            com.example.glucoflow.dataOnline.modelOnline.Meal(500, "test", strMealThumb = "null")
        }
    }
    //API_Repository

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