package com.example.glucoflow.data.model

import com.squareup.moshi.Json

data class Meal (

    @Json(name="idMeal")
    val id: Int,
    //Name
    val strMeal: String,
    val strCategory: String,
    //Land
    val strArea: String,
    //Anleitung
    val strInstructions: String,
    //Zutaten

    val strIngredient1:String? = "",
    val strIngredient2:String? = "",
    val strIngredient3:String? = "",
    val strIngredient4:String? = "",
    val strIngredient5:String? = "",
    val strIngredient6:String? = "",
    val strIngredient8:String? = "",
    val strIngredient9:String? = "",
    //Link
    val strSource: String? = "",
    val strYoutube: String? = "",
    //Foto
    val strMealThumb: String? = ""

)