package com.example.glucoflow.dataOnline.modelOnline

import com.squareup.moshi.Json

data class Meal (
    /**
     * ➔ In unserer Data-Class können wir mit der Annotation @Json und danach mit Angabe des Feldes bestimmen welches Feld wir meinen
     * ➔ Dadurch können wir die Namen der Variablen und Values selbst wählen
     */
    @Json(name="idMeal")
    val id: Int,
    //Name
    val strMeal: String ,
    val strCategory: String = "",
    //Land
    val strArea: String = "",
    //Anleitung
    val strInstructions: String  = "",
    //Zutaten

    val strIngredient1:String? = "",
    val strIngredient2:String? = "",
    val strIngredient3:String? = "",
    val strIngredient4:String? = "",
    val strIngredient5:String? = "",
    val strIngredient6:String? = "",
    val strIngredient7:String? = "",
    val strIngredient8:String? = "",
    val strIngredient9:String? = "",
    val strIngredient10:String? = "",
    val strIngredient11:String? = "",
    val strIngredient12:String? = "",
    val strIngredient13: String? = "",
    val strIngredient14: String? = "",
    val strIngredient15: String? = "",
    val strIngredient16: String? = "",
    val strIngredient17: String? = "",
    val strIngredient18: String? = "",
    val strIngredient19: String? = "",
    val strIngredient20: String? = "",

    //Link
    val strSource: String? = "",
    val strYoutube: String? = "",
    //Foto
    val strMealThumb: String?

)