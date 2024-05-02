package com.example.glucoflow.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.glucoflow.data.model.Meal
import com.example.glucoflow.data.remote.MealApi

const val TAG = "AppRepositoryTAG"

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



}