package com.example.glucoflow.dataRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.glucoflow.dataRoom.model.Meal

@Database(entities = [Meal::class], version = 1)
/**
 *
 * gibt an, dass diese Klasse eine Datenbank ist
 * mit der Tabelle Glucose und sich in der Version 1 befindet
 */
abstract class MealDatabase : RoomDatabase() {

    abstract val mealDatabaseDao: MealDao
    //verknüpft die Datenbank mit dem DAO Interface
}

/**
 * Die Version muss geändert werden wenn man das Schema der Datenbank ändert
 * (z.B.: eine weitere Klasse(Tabelle) einfügt oder weitere Spalten(Variablen))
 */
private lateinit var INSTANCE: MealDatabase

/**
 * Erstellt eine neue Datenbank wenn noch keine in INSTANCE gespeichert wurde.
 * Somit wird sichergestellt dass es nur eine einzige Datenbank gibt.
 */
fun getDatabase3(context: Context):MealDatabase {
    synchronized(MealDatabase::class.java) {
        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MealDatabase::class.java,
                "meal_database"
            )
                .build()
        }
    }
    return INSTANCE
}