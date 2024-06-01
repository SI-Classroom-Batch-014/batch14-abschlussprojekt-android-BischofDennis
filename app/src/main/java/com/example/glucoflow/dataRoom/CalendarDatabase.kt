package com.example.glucoflow.dataRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.glucoflow.dataRoom.model.MyCalendar

@Database(entities = [MyCalendar::class], version = 1)
/**
 *
 * gibt an, dass diese Klasse eine Datenbank ist
 * mit der Tabelle Glucose und sich in der Version 1 befindet
 */
abstract class CalendarDatabase : RoomDatabase() {

    abstract val calendarDatabaseDao: CalendarDao
    //verknüpft die Datenbank mit dem DAO Interface
}

/**
 * Die Version muss geändert werden wenn man das Schema der Datenbank ändert
 * (z.B.: eine weitere Klasse(Tabelle) einfügt oder weitere Spalten(Variablen))
 */
private lateinit var INSTANCE: CalendarDatabase

/**
 * Erstellt eine neue Datenbank wenn noch keine in INSTANCE gespeichert wurde.
 * Somit wird sichergestellt dass es nur eine einzige Datenbank gibt.
 */
fun getDatabase2(context: Context): CalendarDatabase {
    synchronized(CalendarDatabase::class.java) {
        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                CalendarDatabase::class.java,
                "calendar_database"
            )
                .build()
        }
    }
    return INSTANCE
}