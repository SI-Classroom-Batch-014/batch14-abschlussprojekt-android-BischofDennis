package com.example.glucoflow.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.glucoflow.db.model.Glucose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

const val TAG = "Repository"
class GlucoseRepository(private val database: GlucoseDatabase) {

    val glucoseList: LiveData<List<Glucose>> = database.glucoseDatabaseDao.getALLData()


    fun upsertGlucose(glucose: Glucose) {
        database.glucoseDatabaseDao.upsertGlucose(glucose)
    }

    fun deleteGlucose(glucose: Glucose){
        database.glucoseDatabaseDao.deleteGlucose(glucose)
    }

    suspend fun searchGlucose(searchTerm: String): List<Glucose> {
        return database.glucoseDatabaseDao.searchGlucose(searchTerm)
    }

    suspend fun insert(glucose: Glucose) {
        try {
            database.glucoseDatabaseDao.insert(glucose)
        } catch (e: Exception) {
            Log.d(TAG,"Failed to insert into Database: $e")
        }
    }
}