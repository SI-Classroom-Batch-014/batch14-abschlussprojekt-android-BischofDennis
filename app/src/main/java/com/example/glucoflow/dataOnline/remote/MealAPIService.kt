package com.example.glucoflow.dataOnline.remote

import com.example.glucoflow.dataOnline.modelOnline.Data
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**Um unseren API-Service zu nutzen, brauchen wir insgesamt sechs Dinge:
 * 1. Dependencies für Moshi und Retrofit
 * 2. Die Base-URL der API
 * 3. Einen Moshi-Client um die Antworten direkt in Objekte zu verwandeln
 * 4. Einen Retrofit-Client um die Kommunikation mit der API zu übernehmen
 * 5. Ein Interface, das die verschiedenen Endpunkte der API anspricht
 * 6. Das Retrofit-Objekt, das alle oberen Teile vereint
 */
const val BASE_URL = "https://www.themealdb.com/api/json/v2/9973533/"
/**
 * Moshi wird als Value angelegt und im Builder-Pattern geschrieben
 * ➔ Die KotlinJsonAdapterFactory wird hinzugefügt, damit Moshi Json in Kotlin Dateien
 * übersetzen kann
 */
/**➔ Moshi JSON Library
➔ Übernimmt das Übersetzen von JSON in
Kotlin Objekte für uns
➔ Wird im Builder-Pattern geschrieben*/
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
/**
 * ➔ Retrofit wird als Value angelegt und im Builder-Pattern geschrieben
 * ➔ Als Converter Factory fügen wir unseren vorher erstellten Moshi-Client (moshi) hinzu
 * ➔ Die Base-Url wird hinzugefügt
 */
/**Ist ein HTTP Client für Android
 * ➔ Der Retrofit-Service übernimmt die
 * Kommunikation mit dem Server
 * ➔ Benutzt Moshi um die Antworten direkt in Kotlin Objekte zu übersetzen
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**->Ein Interface gibt an, welche Funktionen es geben soll, wenn ein Objekt dieses Interface implementiert (benutzt)
 * ➔ In diesem Fall gibt es nur eine Funktion im Interface, um ein MealResult zu laden
 * ➔ Über die Funktion wird beschrieben, welcher Endpunkt der API angesprochen werden soll
 * ➔ In diesem Fall wird der Endpunkt “random.php” angesprochen mit einer GET-Funktion
 */
interface MealAPIService {

    /**
     * Endpunkt für ein Random Meal wird angegeben.
     */

    @GET("random.php")
    suspend fun getRandomMeal(): Data

    /**
     * ➔ Query = “Abfrage”
     * ➔ Wir fragen spezielle Daten vom Server ab
     * ➔ Durch Queries können wir die Antworten einer API eingrenzen
     * ➔ Anstatt alle Daten zu laden und dann selbst zu sortieren,
     * lassen wir uns einfach nur die Daten zurückgeben, die wir benötigen
     *
     * ➔ Die Query hängt immer an den Endpunkt an und ist durch ein “?” gekennzeichnet
     * ➔ Nach dem Fragezeichen folgt das Ziel der Abfrage (in diesem Fall “c”)
     * ➔ Danach wird mit einem “=” der Wert der Abfrage zugewiesen
     *
     * ➔ Um eine Query zu benutzen können wir einfach unserer Funktion im Interface einen Parameter übergeben
     * ➔ Dieser muss nur mit @Query annotiert werden und in Klammern muss das Ziel der Query angegeben werden
     * ➔ Hierdurch setzt Retrofit dann am Ende unsere Anfrage zusammen
     *
     * ➔ Beispiel in diesem Fall:
     * ◆ Die Base-Url: www.themealdb.com/api/json/v1/1/
     * ◆ Der Endpunkt: filter.php
     * ◆ Die Query: ?c=category
     * ➔ www.themealdb.com/api/json/v1/1/filter.php?c=category
     * ➔ Bei einem Aufruf der Funktion mit Parameter “Beef” käme also heraus:
     * ◆ www.themealdb.com/api/json/v1/1/filter.php?c=Beef
     */
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

/**➔ Das Objekt am Ende vereint all unsere vorher erstellten Dinge
 * ➔ Das Objekt erhält ein Feld retrofitService welches mit Hilfe des Interfaces und des
 * vorher erstellten Retrofit-Builders erstellt wird
 */
object MealApi {
    val retrofitService: MealAPIService by lazy { retrofit.create(MealAPIService::class.java)}
}