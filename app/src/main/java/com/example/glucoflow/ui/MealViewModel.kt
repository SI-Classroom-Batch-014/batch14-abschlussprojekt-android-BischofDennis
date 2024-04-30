package com.example.glucoflow.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoflow.data.AppRepository
import com.example.glucoflow.data.model.Meal
import com.example.glucoflow.data.remote.MealApi
import kotlinx.coroutines.launch

class MealViewModel: ViewModel() {

    private val repository = AppRepository(MealApi)
    val randomMeal = repository.meals
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


    fun loadMeal(search: String) {
        viewModelScope.launch {
            repository.getMeals(search)
        }
    }

    fun setCurrentMeal(meal: Meal) {

        //überprüfung schreiben schleife if empty rausnehmen so wie es aussieht
        // sieht man leere strings nicht

        val ingredientList = mutableListOf(
            meal.strIngredient1!!,
            meal.strIngredient2!!,
            meal.strIngredient3!!,
            meal.strIngredient4!!,
            meal.strIngredient5!!,
            meal.strIngredient6!!,
            meal.strIngredient8!!,
            meal.strIngredient9!!,
            meal.strIngredient10!!,
            meal.strIngredient11!!,
            meal.strIngredient12!!,
            meal.strIngredient13!!,
            meal.strIngredient14!!,
            meal.strIngredient15!!,
            meal.strIngredient16!!,
            meal.strIngredient17!!,
            meal.strIngredient18!!,
            meal.strIngredient19!!,
            meal.strIngredient20!!,
            )

        _currentMeal.value = meal
        //gefüllte liste geht hier in Live data
        _currentMealIngredients.postValue(ingredientList)
        _currentMealInstructions.postValue(meal.strInstructions)
    }
}