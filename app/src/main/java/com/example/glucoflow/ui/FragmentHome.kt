package com.example.glucoflow.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.adapter.GlucoseAdapter
import com.example.glucoflow.adapter.MyCalendarAdapter
import com.example.glucoflow.databinding.FragmentHomeBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class FragmentHome : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    var carbonInsulin = 0
    var carbonMeal = 0
    var carbonGesamt = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setDateToCurrentWeek()

        binding.datumBtn.setOnClickListener {
            showDatePickerDialog()
        }
        binding.profilePicture.setOnClickListener {
            findNavController().navigate(R.id.fragmentProfile)
        }

        //Aktuell eingeloggter User
        viewModel.currentUser.observe(viewLifecycleOwner){
            if (it != null) {
                binding.textViewRegina.text = it.email
            }
        }

        // viewModel.insertGlucose(Glucose(0, "test", "2024-05-08"))

        viewModel.currentDate.observe(viewLifecycleOwner) {

            binding.textViewTodayDate.text = it

        }

        binding.textViewTodayDate.setOnClickListener {
            carbonGesamt = carbonInsulin + carbonMeal
            viewModel.currentDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Heute: $it", Toast.LENGTH_SHORT).show()
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)

                }
            }

        }

        viewModel.viewModelScope.launch {
            viewModel.showGlucoseList()
        }

        binding.glucosewidget.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.showGlucoseList()
            }

        }

        //Glucose Widget
        viewModel.glucoseListoneDay.observe(viewLifecycleOwner) {
            Log.d("Glucose", "${viewModel.glucoseListoneDay.value}")
            binding.rvGlucose.adapter = GlucoseAdapter(it, viewModel)

        }

        //Kohlenhydrate Widget
        viewModel.glucoseListoneDay.observe(viewLifecycleOwner) {
            viewModel.viewModelScope.launch {
                var localCarbongesamt = 0
                // weil es eine Liste ist alles einzeln durchgehen und dann zusammen rechnen
                for (i in it) {
                    localCarbongesamt += i.carbon.toInt()
                }
                carbonInsulin = localCarbongesamt
                binding.textViewKohlenhydrate.text = carbonGesamt.toString()
            }

        }


        //daten beobachten und abrufen von livedata
      /**  viewModel.glucoseCollectionReference.addSnapshotListener{ value, error ->
            if (error == null && value != null){
                //it = value und läuft die liste durch
                val glucose = value.map { it.toObject(GlucoseFirebase::class.java) }
                //wenn was drin ist packt er es in die Lste
                if (glucose != null) {
                    //daten von firebase in die Livedata
                    viewModel.glucoseListoneDayFirebase.value?.addAll(glucose)
                }
            }
        }*/

        //Kalender Widget
        viewModel.mycalendaroneDay.observe(viewLifecycleOwner) {
            Log.d("MyCalendar", "${viewModel.mycalendaroneDay.value}")
            binding.rvMyCalendar.adapter = MyCalendarAdapter(it, viewModel)
        }

        //bei App start
        viewModel.viewModelScope.launch {
            viewModel.filterMyCalendarListToday()

        }
        //Kalender Widget draufklicken alle termine ab heute
        binding.kalenderwidget.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.filterMyCalendarListToday()
                //viewModel.sortDescending()
            }
        }

        viewModel.myMealoneDay.observe(viewLifecycleOwner) {
            Log.d("Meal", "${viewModel.myMealoneDay.value}")
            viewModel.viewModelScope.launch {
                var localCarbongesamt = 0
                var kalorienGesamt = 0
                for (i in it) {
                    kalorienGesamt += i.kalorien.toInt()
                    localCarbongesamt += i.kohlenhydrate.toInt()

                }
                // Addiere die Kohlenhydrate aus den Mahlzeiten zum Gesamtwert

                carbonMeal = localCarbongesamt
                binding.textViewKalorien.text = kalorienGesamt.toString()
            }
        }


        viewModel.mondayDate.observe(viewLifecycleOwner) {
            Log.e("DATE_INFO_DEBUG", "mondayDate: == > $it")
            binding.textViewMondayDate.text = it
        }

        binding.textViewMonday.setOnClickListener {
            Log.w("DATE_INFO_DEBUG", "mondayDate: == > $it")
            viewModel.mondayDate.observe(viewLifecycleOwner) {
                Log.w("DATE_INFO_DEBUG", "mondayDate: == > $it")
                Log.d("Monday Date", "Montag: $it")
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                Toast.makeText(requireContext(), "Montag: $it", Toast.LENGTH_SHORT).show()
                viewModel.viewModelScope.launch {
                    Log.w("DATE_INFO_DEBUG", "mondayDate: == > $it")
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)

                }
            }
            carbonGesamt = carbonInsulin +carbonMeal
        }

        viewModel.tuesdayDate.observe(viewLifecycleOwner) {

            binding.textViewTuesdayDate.text = it
        }

        binding.textViewDienstag.setOnClickListener {
            viewModel.tuesdayDate.observe(viewLifecycleOwner) {
                Log.d("Tuesday Date", "Dienstag: $it")
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                Toast.makeText(requireContext(), "Dienstag: $it", Toast.LENGTH_SHORT).show()
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)

                }
            }
            carbonGesamt = carbonInsulin +carbonMeal
        }


        viewModel.wednesdayDate.observe(viewLifecycleOwner) {
            binding.textViewMittwochDate.text = it
        }

        binding.textViewMittwoch.setOnClickListener {
            viewModel.wednesdayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Mittwoch: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)

                }
            }
            carbonGesamt = carbonInsulin +carbonMeal
        }

        viewModel.thursdayDate.observe(viewLifecycleOwner) {
            binding.textViewDonnerstagDate.text = it
        }

        binding.textViewDonnerstag.setOnClickListener {
            viewModel.thursdayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Donnerstag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)


                }
            }
            carbonGesamt = carbonInsulin +carbonMeal
        }

        viewModel.fridayDate.observe(viewLifecycleOwner) {
            binding.textViewFreitagDate.text = it
        }

        binding.textViewFreitag.setOnClickListener {
            viewModel.fridayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Freitag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)


                }
            }
            carbonGesamt = carbonInsulin +carbonMeal
        }

        viewModel.saturdayDate.observe(viewLifecycleOwner) {
            binding.textViewSamstagDate.text = it
        }

        binding.textViewSamstag.setOnClickListener {
            viewModel.saturdayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Samstag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)
                }
            }
            carbonGesamt = carbonInsulin +carbonMeal
        }

        viewModel.sundayDate.observe(viewLifecycleOwner) {
            binding.textViewSundayDate.text = it
        }

        binding.textViewSonntag.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.filterGlucoseList(it.toString())
                viewModel.filterMyCalendarList(it.toString())
                viewModel.filterMyMealList(it.toString())
            }
            carbonGesamt = carbonInsulin +carbonMeal

            viewModel.sundayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Sonntag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)


                }
            }
            carbonGesamt = carbonInsulin +carbonMeal
        }





    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        // Heute als Standardauswahl festlegen
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        datePickerBuilder.setSelection(today)
        // Einschränkungen für den Kalender
        val constraintsBuilder = CalendarConstraints.Builder()
        //constraintsBuilder.setValidator(DateValidatorPointForward.now()) // Keine Vergangenheitsdaten zulassen
        datePickerBuilder.setCalendarConstraints(constraintsBuilder.build())
        // Erstellen des MaterialDatePicker
        val datePicker = datePickerBuilder.build()
        datePicker.addOnPositiveButtonClickListener {

            // Hier können Sie das ausgewählte Datum verarbeiten
            // 'it' repräsentiert den ausgewählten Zeitstempel in Millisekunden
            //val selectedDate = datePicker.headerText // Datum als formatierter String
            val selectedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()

            // Formatieren des Datums als 'dd.MM.yyyy'
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val formattedDate = selectedDate.format(formatter)

            //val datum = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            // Aktion mit dem ausgewählten Datum
            viewModel.setDate(formattedDate)
            viewModel.setWeekDays(selectedDate)
        }
        // Anzeigen des DatePickers
        datePicker.show(parentFragmentManager, datePicker.toString())
    }





}