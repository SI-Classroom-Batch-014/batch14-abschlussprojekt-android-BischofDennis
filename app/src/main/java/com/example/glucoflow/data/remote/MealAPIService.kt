package com.example.glucoflow.data.remote

import com.example.glucoflow.data.model.Data
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://www.themealdb.com/api/json/v2/9973533/"
/**
 * Moshi wird als Value angelegt und im Builder-Pattern geschrieben
 * ➔ Die KotlinJsonAdapterFactory wird hinzugefügt, damit Moshi Json in Kotlin Dateien
 * übersetzen kann
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
/**
 * ➔ Retrofit wird als Value angelegt und im Builder-Pattern geschrieben
 * ➔ Als Converter Factory fügen wir unseren vorher erstellten Moshi-Client (moshi) hinzu
 * ➔ Die Base-Url wird hinzugefügt
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
interface MealAPIService {

    /**
     * Endpunkt für ein Random Meal wird angegeben.
     */

    @GET("random.php")
    suspend fun getRandomMeal(): Data

    @GET("search.php")
    suspend fun getMealBySearch(@Query("f")search: String): Data

    /**
     * Endpunkt für alle Meal Kategorien.
     */
    @GET("categories.php")
    suspend fun getAllMealCategories(): Data

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): Data

    //wird benötigt, weil der 2te Api Call (Kategorie) anders aufgebaut ist (3 Eigenschaften)
    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: Int): Data

}

object MealApi {
    val retrofitService: MealAPIService by lazy { retrofit.create(MealAPIService::class.java)}
}