package com.example.glucoflow.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoflow.dataOnline.AppRepository
import com.example.glucoflow.dataOnline.modelOnline.Meal
import com.example.glucoflow.dataOnline.remote.MealApi
import com.example.glucoflow.dataRoom.getDatabase
import com.example.glucoflow.dataRoom.getDatabase2
import com.example.glucoflow.dataRoom.getDatabase3
import kotlinx.coroutines.launch

/**
 * ➔ Das ViewModel erstellt das Repository und übergibt dabei den MealApi-Service mit an das Repository
 * ➔ In diesem Beispiel liegt die LiveData im Repository und wird nur vom ViewModel “durchgeschliffen”
 * ➔ loadMeal()
 * ◆ im Hintergrund mittels
 * Coroutine
 */
//class MealViewModel: Viewmodel(){
class MealViewModel(application: Application) : AndroidViewModel(application){

    /**private val repository = AppRepository(MealApi)
     * API-ROOM zusammengemacht
     */

private val repository = com.example.glucoflow.dataRoom.AppRepository(
    (MealApi),
    getDatabase(application),
    getDatabase2(application),
    getDatabase3(application)
)
    val meals = repository.meals
    //val currentMeal = repository.currentMeal
    //var currentMealIndex: Int = 0

    //Live Data für ausgewählten Rezept
    private var _currentMeal = MutableLiveData<Meal>()

    val currentMeal: LiveData<Meal>
        get() = _currentMeal

    //Live Data für ausgewählten Rezept
    private var _currentMealIngredients = MutableLiveData<MutableList<String>>()

    val currentMealIngredients: LiveData<MutableList<String>>
        get() = _currentMealIngredients

    //Live Data für Zutaten von Rezept
    private var _currentMealInstructions = MutableLiveData<String>()

    val currentMealInstructions: LiveData<String>
        get() = _currentMealInstructions

    fun getRandomKH(): String {
        val kH = (0..100).random().toString()
        return kH
    }

    fun getRandomKcal(): String {
        val kcal= (0..1000).random().toString()
        return kcal
    }
    fun loadMeal(search: String) {
        viewModelScope.launch {
            repository.getMealsBySearch(search)

        }
    }

    fun loadRandomMeal() {
        viewModelScope.launch {
            repository.getRandomMeal()
        }
    }

    fun loadMealByCategory(category: String) {
        viewModelScope.launch {
            repository.getMealCategory(category)
        }
    }

    //der Category Call ist anders aufgebaut (3 Eigenschaften) als der normale Call
    fun  getMealbyId(id: Int){
        viewModelScope.launch {
            _currentMeal.value = repository.getMealbyId(id)
            // wird benötigt wegen Ingredient List
            setCurrentMeal(repository.getMealbyId(id))
        }
    }


    fun setCurrentMeal(meal: Meal) {

        //überprüfung schreiben schleife if empty rausnehmen so wie es aussieht
        // sieht man leere strings nicht

        val ingredientList = mutableListOf<String?>(
            meal.strIngredient1,
            meal.strIngredient2,
            meal.strIngredient3,
            meal.strIngredient4,
            meal.strIngredient5,
            meal.strIngredient6,
            meal.strIngredient7,
            meal.strIngredient8,
            meal.strIngredient9,
            meal.strIngredient10,
            meal.strIngredient11,
            meal.strIngredient12,
            meal.strIngredient13,
            meal.strIngredient14,
            meal.strIngredient15,
            meal.strIngredient16,
            meal.strIngredient17,
            meal.strIngredient18,
            meal.strIngredient19,
            meal.strIngredient20
        ).filterNotNull().filter { it.isNotBlank() }

        _currentMeal.value = meal
        //gefüllte liste geht hier in Live data
        _currentMealIngredients.postValue(ingredientList.toMutableList())
        _currentMealInstructions.postValue(meal.strInstructions)
    }
}