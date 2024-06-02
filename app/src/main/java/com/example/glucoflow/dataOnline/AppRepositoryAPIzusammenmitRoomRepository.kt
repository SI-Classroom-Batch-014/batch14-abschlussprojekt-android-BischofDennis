package com.example.glucoflow.dataOnline

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.glucoflow.dataOnline.modelOnline.Meal
import com.example.glucoflow.dataOnline.remote.MealApi

const val TAG = "AppRepositoryTAG"

/**
 * ➔ Das Repository bekommt ein Objekt vom Typ MealApi übergeben
 * ➔ Danach kann es mit Hilfe dieses Objektes die Anfrage an die API stellen
 */

//API-ROOM zusammengemacht wird nicht mehr benötigt
class AppRepository(private val mealAPI : MealApi) {
    var index = 0

    private var _meals = MutableLiveData<List<Meal>>()
    val meals : LiveData<List<Meal>>
        get() = _meals


    suspend fun getMealsBySearch(search: String){
        try {
            val result = mealAPI.retrofitService.getMealBySearch(search)
            _meals.value = result.meals.shuffled()
            //alternative _memes.postValue(result.memesList)
        }catch (e: Exception){
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }


    suspend fun getRandomMeal(){
        try {
            val result = mealAPI.retrofitService.getRandomMeal()
            _meals.value = result.meals.shuffled()
            //alternative _memes.postValue(result.memesList)
        }catch (e: Exception){
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }

    suspend fun getMealCategory(category: String){
        try {
            val result = mealAPI.retrofitService.getMealsByCategory(category)
            _meals.value = result.meals.shuffled()
            //alternative _memes.postValue(result.memesList)
        }catch (e: Exception){
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }

    //der Category Call ist anders aufgebaut (3 Eigenschaften) als der normale Call
    suspend fun getMealbyId(id: Int): Meal {
        return try {

            mealAPI.retrofitService.getMealById(id).meals.first()
        }catch (e: Exception){
            Log.e(TAG, "Error loading Data from API: $e")
            Meal(500, "test", strMealThumb = "null")
        }
    }



}