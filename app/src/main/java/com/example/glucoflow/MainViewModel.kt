package com.example.glucoflow

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.glucoflow.data.model.Profile
import com.example.glucoflow.db.AppRepository
import com.example.glucoflow.db.getDatabase
import com.example.glucoflow.db.getDatabase2
import com.example.glucoflow.db.getDatabase3
import com.example.glucoflow.db.model.Glucose
import com.example.glucoflow.db.model.Meal
import com.example.glucoflow.db.model.MyCalendar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * Da wir im ViewModel den Kontext brauchen, um eine Datenbank-Instanz zu erzeugen,
 * erbt unser ViewModel nun nicht mehr von ViewModel() sondern von AndroidViewModel(),
 * da dieses uns den application-context mitgibt
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    //database Room
    /**
     * ruft Datenbank auf und erstellt falls noch keine vorhanden
     * ist eine neue mittels application(context)
     */
    val repository = AppRepository(
        getDatabase(application),
        getDatabase2(application),
        getDatabase3(application)
    )

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

    val carbonHydrateoneDay: LiveData<Glucose>
        get() = _carbonhydrateoneDay



    //Kalender
    private var _myCalendarList = MutableLiveData<MutableList<MyCalendar>>()

    val myCalendarList: LiveData<MutableList<MyCalendar>>
        get() = _myCalendarList

    private var _mycalendaroneDay = MutableLiveData<MutableList<MyCalendar>>()

    val mycalendaroneDay: LiveData<MutableList<MyCalendar>>
        get() = _mycalendaroneDay


    //Meal Kalorien Kohlenhydrate
    private var _myMealList = MutableLiveData<MutableList<Meal>>()

    val myMealList: LiveData<MutableList<Meal>>
        get() = _myMealList

    private var _myMealoneDay = MutableLiveData<MutableList<Meal>>()

    val myMealoneDay: LiveData<MutableList<Meal>>
        get() = _myMealoneDay



    fun insertGlucose(glucose: Glucose) {
        viewModelScope.launch {
            repository.insert(glucose)
        }
    }

    /**
     * insertGlucose(glucose: Glucose)
     * startet die insert Funktion des Repositories
     * in einer Coroutine um den UI Thread nicht zu blockieren
     */

    fun insertCalendar(calender: MyCalendar) {
        viewModelScope.launch {
            repository.insertCalendar(calender)
        }
    }

    //Kalorien widget
    fun saveKhKcal(meal: Meal) {
        viewModelScope.launch {
            repository.saveKhKcal(meal)
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


    suspend fun filterGlucoseList(day: String) {
        viewModelScope.launch {
            _glucoseList.value = repository.searchGlucoseAll().toMutableList()
            _glucoseListoneDay.value = _glucoseList.value?.filter {
                it.dateTime.contains(day)
            }?.toMutableList()
        }
    }

    suspend fun filterMyCalendarList(day: String) {
        viewModelScope.launch {
            _myCalendarList.value = repository.searchMyCalendarAll().toMutableList()
            _mycalendaroneDay.value = _myCalendarList.value?.filter {
                it.date.contains(day)
            }?.toMutableList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun filterMyCalendarListToday() {
        val currentDate = LocalDate.now() // Heutiges Datum
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        viewModelScope.launch {
            _myCalendarList.value = repository.searchMyCalendarAll().toMutableList()
            _mycalendaroneDay.value = _myCalendarList.value?.filter {
                // Wenn LocalDatenach currentDate kommt true raus
                LocalDate.parse(it.date, formatter).isAfter(currentDate) || LocalDate.parse(it.date, formatter).equals(currentDate)
            }?.toMutableList()?.sortedBy { LocalDate.parse(it.date, formatter) }?.toMutableList()

            Log.i("_mycalendaroneDay", "${_mycalendaroneDay.value}")
        }
    }

    /**@RequiresApi(Build.VERSION_CODES.O)
    suspend fun sortAscending(){
        val currentDate = LocalDate.now()//Heutiges Datum
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        //sortedby = neue liste
        _mycalendaroneDay.value = _mycalendaroneDay.value?.sortedBy {
            LocalDate.parse(it.date, formatter)
        }?.toMutableList()
    }*/

    suspend fun filterMyMealList(day: String) {
        viewModelScope.launch {
            _myMealList.value = repository.searchMealAll().toMutableList()
            _myMealoneDay.value = _myMealList.value?.filter {
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
        calendar.set(
            Calendar.DAY_OF_WEEK,
            Calendar.MONDAY
        ) // Setze auf den Montag der aktuellen Woche
        calendar.add(
            Calendar.WEEK_OF_YEAR,
            0
        ) // Gehe eine Woche zurück, um den letzten Montag zu finden
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _mondayDate = MutableLiveData<String>()
    val mondayDate: LiveData<String>
        get() = _mondayDate


    private fun getTuesdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, 0)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _tuesdayDate = MutableLiveData<String>()
    val tuesdayDate: LiveData<String>
        get() = _tuesdayDate


    private fun getWednesdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, 0)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _wednesDate = MutableLiveData<String>()
    val wednesdayDate: LiveData<String>
        get() = _wednesDate

    private fun getThursdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, 0)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _thursdayDate = MutableLiveData<String>()
    val thursdayDate: LiveData<String>
        get() = _thursdayDate

    private fun getFridayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, 0)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _fridayDate = MutableLiveData<String>()
    val fridayDate: LiveData<String>
        get() = _fridayDate

    private fun getSaturdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, 0)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _saturdayDate = MutableLiveData<String>()
    val saturdayDate: LiveData<String>
        get() = _saturdayDate

    private fun getSundayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, 0)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private var _sundayDate = MutableLiveData<String>()
    val sundayDate: LiveData<String>
        get() = _sundayDate


    private val _selectedGlucose = MutableLiveData<Glucose>()
    val selectedGlucose: LiveData<Glucose>
        get() = _selectedGlucose

    fun selectedGlucose(glucose: Glucose) {
        _selectedGlucose.value = glucose
    }

    fun upsertGlucose(glucose: Glucose) {
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
    //update firestore
    private val firebaseStore = Firebase.firestore

    private var _currentUser = MutableLiveData<FirebaseUser>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    val profileCollectionReference: CollectionReference = firebaseStore.collection("profiles")
    private lateinit var profileDocumentReference: DocumentReference

    init {
        if (firebaseAuth.currentUser != null) {
            setProfileDocumentReference()
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



    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    _currentUser.value = firebaseAuth.currentUser
                    setProfileDocumentReference()
                } else {
                    Log.e("AUTH", "register ${authResult.exception?.message.toString()}")
                }
            }
        }
    }

    private fun setProfileDocumentReference() {
        profileDocumentReference = profileCollectionReference.document(firebaseAuth.currentUser!!.uid)
    }

    fun register(email: String, password: String, username: String) {
        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    _currentUser.value = firebaseAuth.currentUser
                    setProfileDocumentReference()
                    profileDocumentReference.set(Profile(username = username))
                } else {
                    Log.e("AUTH", "register ${authResult.exception?.message.toString()}")
                }
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = firebaseAuth.currentUser
    }


    fun updateProfile(profile: Profile) {

        this.profileDocumentReference.set(profile)

    }
}