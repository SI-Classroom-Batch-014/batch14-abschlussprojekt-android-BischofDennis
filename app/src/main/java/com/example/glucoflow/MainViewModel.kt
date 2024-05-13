package com.example.glucoflow
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.glucoflow.data.Profile
import com.example.glucoflow.db.AppRepository
import com.example.glucoflow.db.getDatabase
import com.example.glucoflow.db.getDatabase2
import com.example.glucoflow.db.model.Glucose
import com.example.glucoflow.db.model.MyCalendar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    val repository = AppRepository(getDatabase(application), getDatabase2(application))

    /**
     * In der glucoseList Value wird der Wert der guestList des Repositories gespeichert
     * (dabei handelt es sich immer noch um LiveData)
     */

    private var _glucoseList = MutableLiveData<MutableList<Glucose>>()

    val glucoseList: LiveData<MutableList<Glucose>>
        get() = _glucoseList


    private var _glucoseListoneDay = MutableLiveData<MutableList<Glucose>>()

    val glucoseListoneDay: LiveData<MutableList<Glucose>>
        get() = _glucoseListoneDay

    private var _carbonhydrateoneDay = MutableLiveData<Glucose>()

    val carbonHydrateoneDay : LiveData<Glucose>
        get() = _carbonhydrateoneDay

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

    fun insertCalendar(calender: MyCalendar){
        viewModelScope.launch {
            repository.insertCalendar(calender)
        }
    }



   /** fun filterGlucoseList(day: String){
        getGlucoseList()
        _glucoseListoneDay.value =_glucoseList.value?.filter {
            it.dateTime.contains(day)
        }?.toMutableList()
    }

   fun getGlucoseList(){
       viewModelScope.launch {
           _glucoseList.value = repository.searchGlucoseAll().toMutableList()
       }
   }*/


    suspend fun filterGlucoseList(day: String){
        viewModelScope.launch {
            _glucoseList.value = repository.searchGlucoseAll().toMutableList()
            _glucoseListoneDay.value = _glucoseList.value?.filter {
                it.dateTime.contains(day)
            }?.toMutableList()
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    //Aktuelles Datum als LiveData
    // ändern


    private var _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String>
        get() = _currentDate


    private fun getMondayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) // Setze auf den Montag der aktuellen Woche
        calendar.add(Calendar.WEEK_OF_YEAR, -1) // Gehe eine Woche zurück, um den letzten Montag zu finden
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _mondayDate = MutableLiveData<String>()
    val mondayDate: LiveData<String>
        get() = _mondayDate


    private fun getTuesdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _tuesdayDate = MutableLiveData<String>()
    val tuesdayDate: LiveData<String>
        get() = _tuesdayDate


    private fun getWednesdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _wednesDate = MutableLiveData<String>()
    val wednesdayDate: LiveData<String>
        get() = _wednesDate

    private fun getThursdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _thursdayDate = MutableLiveData<String>()
    val thursdayDate: LiveData<String>
        get() = _thursdayDate

    private fun getFridayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _fridayDate = MutableLiveData<String>()
    val fridayDate: LiveData<String>
        get() = _fridayDate

    private fun getSaturdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _saturdayDate = MutableLiveData<String>()
    val saturdayDate: LiveData<String>
        get() = _saturdayDate

    private fun getSundayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _sundayDate = MutableLiveData<String>()
    val sundayDate: LiveData<String>
        get() = _sundayDate


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
        //werte Value werden sofort gesetzt
        _currentDate.value = getCurrentDate()
        _mondayDate.value = getMondayDate()
        _tuesdayDate.value = getTuesdayDate()
        _wednesDate.value = getWednesdayDate()
        _thursdayDate.value = getThursdayDate()
        _fridayDate.value = getFridayDate()
        _saturdayDate.value = getSaturdayDate()
        _sundayDate.value = getSundayDate()
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