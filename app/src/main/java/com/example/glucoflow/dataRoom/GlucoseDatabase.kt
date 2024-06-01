package com.example.glucoflow.dataRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.glucoflow.dataRoom.model.Glucose

@Database(entities = [Glucose::class], version = 1)
/**
 *
 * gibt an, dass diese Klasse eine Datenbank ist
 * mit der Tabelle Glucose und sich in der Version 1 befindet
 */
abstract class GlucoseDatabase : RoomDatabase() {

    abstract val glucoseDatabaseDao: GlucoseDao
    //verknüpft die Datenbank mit dem DAO Interface
}

/**
 * Die Version muss geändert werden wenn man das Schema der Datenbank ändert
 * (z.B.: eine weitere Klasse(Tabelle) einfügt oder weitere Spalten(Variablen))
 */
private lateinit var INSTANCE: GlucoseDatabase

/**
 * Erstellt eine neue Datenbank wenn noch keine in INSTANCE gespeichert wurde.
 * Somit wird sichergestellt dass es nur eine einzige Datenbank gibt.
 */
fun getDatabase(context: Context): GlucoseDatabase {
    synchronized(GlucoseDatabase::class.java) {
        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                GlucoseDatabase::class.java,
                "glucose_database"
            )
                .build()
        }
    }
    return INSTANCE
}