package com.example.glucoflow

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.glucoflow.dataOnline.modelOnline.CalendarFirebase
import com.example.glucoflow.dataOnline.modelOnline.Chat
import com.example.glucoflow.dataOnline.modelOnline.GlucoseFirebase
import com.example.glucoflow.dataOnline.modelOnline.MealFirebase
import com.example.glucoflow.dataOnline.modelOnline.Message
import com.example.glucoflow.dataOnline.modelOnline.Profile
import com.example.glucoflow.dataOnline.remote.MealApi
import com.example.glucoflow.dataRoom.AppRepository
import com.example.glucoflow.dataRoom.getDatabase
import com.example.glucoflow.dataRoom.getDatabase2
import com.example.glucoflow.dataRoom.getDatabase3
import com.example.glucoflow.dataRoom.model.Glucose
import com.example.glucoflow.dataRoom.model.Meal
import com.example.glucoflow.dataRoom.model.MyCalendar
import com.example.glucoflow.utils.Debug
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Locale

/**
 * Da wir im ViewModel den Kontext brauchen, um eine Datenbank-Instanz zu erzeugen,
 * erbt unser ViewModel nun nicht mehr von ViewModel() sondern von AndroidViewModel(),
 * da dieses uns den application-context mitgibt
 */

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Das ist die MasterSuppe
     * @private val firebaseStore = Firebase.firestore
     * @private val firebaseAuth = Firebase.auth
     */
    private val firebaseStore = Firebase.firestore
    private val firebaseAuth = Firebase.auth

    //Profil liste
    private lateinit var profileDocumentReference: DocumentReference
    val profileCollectionReference: CollectionReference = firebaseStore.collection("profiles")
    //Chat liste
    lateinit var currentChatDocumentReference: DocumentReference
    //Glucose Online
    lateinit var glucoseDocumentReference: DocumentReference
    val glucoseCollectionReference: CollectionReference = firebaseStore.collection("glucoseList")
    //Calendar Online
    lateinit var calendarDocumentReference: DocumentReference
    val calendarCollectionReference: CollectionReference = firebaseStore.collection("calendarList")
    //KH/Kcal Online
    lateinit var mealDocumentReference: DocumentReference
    val mealCollectionReference: CollectionReference = firebaseStore.collection("khkcalList")

    private fun setProfileDocumentReference() {
        profileDocumentReference =
            profileCollectionReference.document(firebaseAuth.currentUser!!.uid)
    }
    private fun setGlucoseDocumentReference() {
        calendarDocumentReference =
            calendarCollectionReference.document(firebaseAuth.currentUser!!.uid)
    }

    private fun setCalendarDocumentReference() {
        glucoseDocumentReference =
            glucoseCollectionReference.document(firebaseAuth.currentUser!!.uid)
    }

    private fun setMealDocumentReference() {
        mealDocumentReference =
            mealCollectionReference.document(firebaseAuth.currentUser!!.uid)
    }

    private var _glucoseListFirebase = MutableLiveData<MutableList<GlucoseFirebase>>()
    val glucoseListFirebase: LiveData<MutableList<GlucoseFirebase>>
        get() = _glucoseListFirebase

    private var _glucoseListOneDayFirebase = MutableLiveData<MutableList<GlucoseFirebase>>()
    val glucoseListOneDayFirebase: LiveData<MutableList<GlucoseFirebase>>
        get() = _glucoseListOneDayFirebase
    fun setGlucoseOnline(glucose: GlucoseFirebase) {
    //dokument erstellen
     glucoseDocumentReference = glucoseCollectionReference.document()
      this.glucoseDocumentReference.set(glucose)
    }

    private var _calendarListFirebase = MutableLiveData<MutableList<CalendarFirebase>>()
    val calendarListFirebase: LiveData<MutableList<CalendarFirebase>>
        get() = _calendarListFirebase

    private var _calendarListOneDayFirebase = MutableLiveData<MutableList<CalendarFirebase>>()
    val calendarListoneDayFirebase: LiveData<MutableList<CalendarFirebase>>
        get() = _calendarListOneDayFirebase
    fun setCalendarOnline(calendar: CalendarFirebase) {
        //dokument erstellen
        calendarDocumentReference = calendarCollectionReference.document()
        this.calendarDocumentReference.set(calendar)
    }
    private var _mealListFirebase = MutableLiveData<MutableList<MealFirebase>>()
    val mealListFirebase: LiveData<MutableList<MealFirebase>>
        get() = _mealListFirebase

    private var _mealListOneDayFirebase = MutableLiveData<MutableList<MealFirebase>>()
    val mealListOneDayFirebase: LiveData<MutableList<MealFirebase>>
        get() = _mealListOneDayFirebase

    fun saveKhKcalOnline(mealFirebase: MealFirebase) {
        //dokument erstellen
        mealDocumentReference = mealCollectionReference.document()
        this.mealDocumentReference.set(mealFirebase)
    }

    private var _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private var _chatPartner = MutableLiveData<String>()
    val chatPartner: LiveData<String>
        get() = _chatPartner

    private var _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String>
        get() = _currentDate

    //LOGIN/REGISTER
    private var _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult>
        get() = _authResult

    /**Der Code für _authResult und die AuthResult-Datenklasse wird verwendet,
     *um den Status von Authentifizierungsoperationen (wie Login oder Registrierung)
     *in deinem ViewModel zu verfolgen und zu kommunizieren.*/
    data class AuthResult(val isSuccessful: Boolean, val errorMessage: String? = null)

    private var _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?>
        get() = _toastMessage

    init {
        // Initialisiere firebaseAuth vor der Verwendung
        if (firebaseAuth.currentUser != null) {
            setProfileDocumentReference()
            setGlucoseDocumentReference()
            setCalendarDocumentReference()
            setMealDocumentReference()
        }
        _currentDate.value = getCurrentDate()
        //setDateToCurrentWeek()
    }

    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        _currentUser.value = firebaseAuth.currentUser
                        setProfileDocumentReference()
                        _authResult.value = AuthResult(true)
                    } else {
                        _authResult.value =
                            AuthResult(false, authResult.exception?.message.toString())
                        Log.e("AUTH", "login ${authResult.exception?.message.toString()}")
                    }
                }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = null//firebaseAuth.currentUser
    }

    fun register(email: String, password: String, username: String) {
        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { authResult ->
                    Log.e("AUTH", "register $authResult")
                    if (authResult.isSuccessful) {
                        _currentUser.value = firebaseAuth.currentUser
                        setProfileDocumentReference()
                        profileDocumentReference.set(Profile(username = username))
                        _authResult.value = AuthResult(true)
                    } else {
                        _authResult.value =
                            AuthResult(false, authResult.exception?.message.toString())
                        handleError(authResult.exception?.message.toString())
                        Log.e("AUTH", "register ${authResult.exception?.message.toString()}")
                    }
                }
        } else {
            _authResult.value = AuthResult(false, "Please enter all entry fields")
        }
    }

    fun sendMessage(message: String) {
        val newMessage = Message(message, firebaseAuth.currentUser!!.uid)
        currentChatDocumentReference.update("messages", FieldValue.arrayUnion(newMessage))
    }

    //damit  die cokunation immer gleich ist, sonst steht abc=xyz und xyz=abc spiegelverkehrt
    private fun createChatId(id1: String, id2: String): String {
        val ids = listOf(id1, id2).sorted()
        return ids.first() + ids.last()
    }

    //Richtigen chat aus der Message liste holen
    fun setCurrentChat(chatPartnerId: String) {
        val chatId = createChatId(chatPartnerId, currentUser.value!!.uid)
        currentChatDocumentReference = firebaseStore.collection("chats").document(chatId)
        currentChatDocumentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null && !task.result.exists()) {
                currentChatDocumentReference.set(Chat())
            }
        }
    }

    //ChatPartner Name
    fun setChatPartnerName(chatPartnerName: String) {
        _chatPartner.value = chatPartnerName
    }

    fun resetToastMessage() {
        _toastMessage.value = null
    }

    private fun handleError(message: String) {
        setToastMessage(message)
        logError(message)
    }

    private fun setToastMessage(message: String) {
        _toastMessage.value = message
    }

    private fun logError(message: String) {
        Log.e(Debug.AUTH_TAG.value, message)
    }

    fun updateProfile(profile: Profile) {
        this.profileDocumentReference.set(profile)
    }

    /** DATABASE-ROOM
     * ruft Datenbank auf und erstellt falls noch keine vorhanden
     * ist eine neue mittels application(context)
     */

    /**API-ROOM zusammengemacht
     * val repository = AppRepository(
     *         getDatabase(application),
     *         getDatabase2(application),
     *         getDatabase3(application)
     *     )
     */
    val repository = AppRepository(
        (MealApi),
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

    suspend fun showGlucoseList() {
        viewModelScope.launch {
            val glucoseData = repository.searchGlucoseAll().toMutableList()
            _glucoseList.value = glucoseData
            _glucoseListoneDay.value = _glucoseList.value
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

    suspend fun filterMyCalendarListToday() {
        val currentDate = LocalDate.now() // Heutiges Datum
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        viewModelScope.launch {
            _myCalendarList.value = repository.searchMyCalendarAll().toMutableList()
            _mycalendaroneDay.value = _myCalendarList.value?.filter {
                // Wenn LocalDatenach currentDate kommt true raus
                LocalDate.parse(it.date, formatter).isAfter(currentDate) || LocalDate.parse(
                    it.date,
                    formatter
                ).equals(currentDate)
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

    //Aktuelles Datum als LiveData ändern
    fun setDate(date: String) {
        _currentDate.value = date
    }

    fun setWeekDays(selectedDate: LocalDate) {
        // Berechne das Datum des Montags der gleichen Woche
        val monday = selectedDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
        // Erstelle eine Liste mit den Daten von Montag bis Sonntag
        val weekDates = (0..6).map { dayOffset ->
            monday.plusDays(dayOffset.toLong())
        }
        // Formatieren der Daten
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        setLiveDataWeekDays(weekDates, formatter)
    }

    fun setDateToCurrentWeek() {
        // Formatieren der Daten
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val today = LocalDate.parse(_currentDate.value, formatter)
        val monday = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
        // Erstelle eine Liste mit den Daten von Montag bis Sonntag
        val weekDates = (0..6).map { dayOffset ->
            monday.plusDays(dayOffset.toLong())
        }
        setLiveDataWeekDays(weekDates, formatter)
    }

    private fun setLiveDataWeekDays(
        weekDates: List<LocalDate>,
        formatter: DateTimeFormatter?
    ) {
        val formattedWeekDates = weekDates.map { date ->
            date.format(formatter)
        }
        Log.i("DATE_INFO_DEBUG", "setLiveDataWeekDays.mondayDate: == > $formattedWeekDates")
        if (formattedWeekDates[0] != null) {
            Log.wtf("DATE_INFO_DEBUG", "formattedWeekDates[0]: == > ${formattedWeekDates.first()}")
            Log.wtf("DATE_INFO_DEBUG", "formattedWeekDates[0]: == > ${formattedWeekDates[0]}")
            Log.wtf("DATE_INFO_DEBUG", "_mondayDate.valu: == > ${_mondayDate}")
            Log.wtf("DATE_INFO_DEBUG", "_mondayDate: == > ${_mondayDate.value}")
            _mondayDate.value = formattedWeekDates[0]
        }
        if (formattedWeekDates[1] != null) {
            _tuesdayDate.value = formattedWeekDates[1]
        }
        if (formattedWeekDates[2] != null) {
            _wednesDate.value = formattedWeekDates[2]
        }
        if (formattedWeekDates[3] != null) {
            _thursdayDate.value = formattedWeekDates[3]
        }
        if (formattedWeekDates[4] != null) {
            _fridayDate.value = formattedWeekDates[4]
        }
        if (formattedWeekDates[5] != null) {
            _saturdayDate.value = formattedWeekDates[5]
        }
        if (formattedWeekDates[6] != null) {
            _sundayDate.value = formattedWeekDates[6]
        }
    }

    private var _mondayDate = MutableLiveData<String>("")
    val mondayDate: LiveData<String>
        get() = _mondayDate

    private var _tuesdayDate = MutableLiveData<String>("")
    val tuesdayDate: LiveData<String>
        get() = _tuesdayDate

    private var _wednesDate = MutableLiveData<String>()
    val wednesdayDate: LiveData<String>
        get() = _wednesDate

    private var _thursdayDate = MutableLiveData<String>()
    val thursdayDate: LiveData<String>
        get() = _thursdayDate

    private var _fridayDate = MutableLiveData<String>()
    val fridayDate: LiveData<String>
        get() = _fridayDate

    private var _saturdayDate = MutableLiveData<String>()
    val saturdayDate: LiveData<String>
        get() = _saturdayDate

    private var _sundayDate = MutableLiveData<String>()
    val sundayDate: LiveData<String>
        get() = _sundayDate

    private val _selectedGlucose = MutableLiveData<Glucose>()
    val selectedGlucose: LiveData<Glucose>
        get() = _selectedGlucose

    private val _filteredGlucoseValue: MutableLiveData<List<Glucose>> = MutableLiveData()
    val filteredGlucoseValue: LiveData<List<Glucose>>
        get() = _filteredGlucoseValue

    fun searchGlucoseValue(searchTerm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _filteredGlucoseValue.postValue(repository.searchGlucose(searchTerm))
        }
    }

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
}