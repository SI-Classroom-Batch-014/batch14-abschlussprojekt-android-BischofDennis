package com.example.glucoflow.dataRoom

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.glucoflow.dataRoom.model.Glucose

@Dao
interface GlucoseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertGlucose(glucose: Glucose)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertALL(glucose: List<Glucose>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(glucose: Glucose)

    /**
     * ➔ Relational – eine Beziehung darstellend
     * ➔ Tabelle kann Verweis auf andere Tabelle
     * haben
     * ➔ Datenbank ist eine einfach strukturierte Sammlung von Daten
     * ➔ In Apps häufig verwendet um Daten beim nächsten Öffnen schnell zur Verfügung zu stellen (auch offline)
     * Quelle: https://openbook.rheinwerk-verlag.de/it_handbuch/12_001.html
     *
     * ➔ Tabellen sind ähnlich wie Klassen in Kotlin
     * ➔ Spalten sind wie Klassenvariablen und benötigen auch einen Namen und einen Datentyp
     * ➔ Reihen sind wie Instanzen (konkrete Ausführung) einer Klasse
     * ➔ Jede Reihe benötigt einen Primärschlüssel (primary key) der meistens Id genannt wird
     *
     * ➔ SQL = Structured Query Language
     * ➔ Wird benötigt um Informationen von relationaler
     * Datenbank abzurufen
     * ➔ Diese Abfragen nennen wir Queries
     *
     * ➔ SELECT - ruft spezifische Informationen aus Datentabelle ab und Ergebnisse können gefiltert werden
     * ➔ INSERT - fügt eine neue Zeile in eine Tabelle
     * ➔ UPDATE - aktualisiert eine vorhandener Zeile
     * ➔ DELETE - entfernt vorhandener Zeile Zeilen von Tabelle
     *
     * ➔ Mit dem App Inspector können wir uns in Android Studio die Datenbank einer App ansehen
     *
     * ➔ SELECT Anweisungen um ein oder mehrere bestimmte Filme abzufragen
     * ➔ INSERT Anweisung um einen neuen Film hinzuzufügen
     * ➔ UPDATE Anweisung um vorhandene Film zu ändern
     * ➔ DELETE Anweisung um ein oder mehrere Filme zu löschen
     *
     * SELECT * FROM calendar_table
     * ➔ Wähle alle Spalten der CalendarDB aus ohne eine bestimmte Auswahl an Zeilen zu treffen
     */
    @Query("SELECT * from Glucose")
    fun getALLData(): LiveData<List<Glucose>>

    @Query("DELETE from Glucose")
    fun deleteALL()

    @Delete
    fun deleteGlucose(glucose: Glucose)

    //
    @Query("SELECT * from Glucose WHERE instr(glucosevalue, :searchTerm)")
    suspend fun searchGlucose(searchTerm: String) : List<Glucose>

    @Query("SELECT * from Glucose")
    suspend fun searchGlucoseAll() : List<Glucose>


}