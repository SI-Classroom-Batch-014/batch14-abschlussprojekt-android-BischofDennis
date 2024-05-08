package com.example.glucoflow

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glucoflow.data.Profile
import com.example.glucoflow.db.GlucoseRepository
import com.example.glucoflow.db.getDatabase
import com.example.glucoflow.db.model.Glucose
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Da wir im ViewModel den Kontext brauchen, um eine Datenbank-Instanz zu erzeugen,
 * erbt unser ViewModel nun nicht mehr von ViewModel() sondern von AndroidViewModel(),
 * da dieses uns den application-context mitgibt
 */
class MainViewModel(application: Application): AndroidViewModel(application) {

    //database Room
    /**
     * ruft Datenbank auf und erstellt falls noch keine vorhanden
     * ist eine neue mittels application(context)
     */
    val repository = GlucoseRepository(getDatabase(application))

    val glucoseList = repository.glucoseList

    /**
     * In der glucoseList Value wird der Wert der guestList des Repositories gespeichert
     * (dabei handelt es sich immer noch um LiveData)
     */

    fun insertGlucose(glucose: Glucose){
        viewModelScope.launch {
            repository.insert(glucose)
        }
    }

    /**
     * insertGlucose(glucose: Glucose)
     * startet die insert Funktion des Repositories
     * in einer Coroutine um den UI Thread nicht zu blockieren
     */

    private val _selectedGlucose = MutableLiveData<Glucose>()
    val selectedGlucose: LiveData<Glucose>
        get() = _selectedGlucose

    fun selectedGlucose(glucose: Glucose){
        _selectedGlucose.value = glucose
    }

    fun upsertGlucose(glucose: Glucose){
        if (glucose.glucosevalue.isNotEmpty() && glucose.dateTime.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.upsertGlucose(glucose)
            }
        }
    }

    fun deleteGlucose(glucose: Glucose) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGlucose(glucose)
        }

    }

    private val _filteredGlucoseValue: MutableLiveData<List<Glucose>> = MutableLiveData()
    val filteredGlucoseValue: LiveData<List<Glucose>>
        get() = _filteredGlucoseValue

    fun searchGlucoseValue(searchTerm: String) {

        viewModelScope.launch(Dispatchers.IO) {
            _filteredGlucoseValue.postValue(repository.searchGlucose(searchTerm))
        }

    }

     // Instanz von Firebase Authentication
     // Ersetzt in diesem Fall ein Repository
    // private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuth = Firebase.auth

    //update firestire
    private val db = Firebase.firestore

    private var _currentUser = MutableLiveData<FirebaseUser>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    lateinit var profileRef: DocumentReference

    init {
        if (firebaseAuth.currentUser != null){
            setProfileRef()
        }
    }



    fun loginWithEmailAndPassword(email: String, pwd: String, completion: () -> Unit) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    completion()
                    //
                    firebaseAuth.currentUser?.sendEmailVerification()

                    profileRef = db.collection("profiles").document(firebaseAuth.currentUser!!.uid)
                    profileRef.set(Profile())

                    db.collection("profiles").document(firebaseAuth.currentUser!!.uid).set(Profile())

                    _currentUser.value = firebaseAuth.currentUser
                    profileRef = db.collection("profiles").document(firebaseAuth.currentUser!!.uid)
                } else {
                    Log.e("FIREBASE_AUTH", authResult.exception.toString())
                }
            }
        }
    }

    private fun setProfileRef() {

    }

    fun registerWithEmailAndPassword(email: String, pwd: String, completion: () -> Unit) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    completion()
                } else {
                    Log.e("FIREBASE_AUTH", authResult.exception.toString())
                }
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }


    fun updateProfile(profile: Profile){

        profileRef.set(profile)

    }
}