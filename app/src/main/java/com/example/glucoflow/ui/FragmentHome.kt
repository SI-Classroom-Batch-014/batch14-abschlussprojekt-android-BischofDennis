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
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.adapter.GlucoseAdapter
import com.example.glucoflow.adapter.MyCalendarAdapter
import com.example.glucoflow.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

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

        // viewModel.insertGlucose(Glucose(0, "test", "2024-05-08"))

        viewModel.currentDate.observe(viewLifecycleOwner) {

            binding.textViewTodayDate.text = it
        }

        binding.textViewTodayDate.setOnClickListener {
            carbonGesamt = carbonInsulin +carbonMeal
            viewModel.currentDate.observe(viewLifecycleOwner) {
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                    viewModel.filterMyMealList(it)

                }
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

        //Kalender Widget
        viewModel.mycalendaroneDay.observe(viewLifecycleOwner) {
            Log.d("MyCalendar", "${viewModel.mycalendaroneDay.value}")
            binding.rvMyCalendar.adapter = MyCalendarAdapter(it, viewModel)
        }

        binding.kalenderwidget.setOnClickListener {
            viewModel.viewModelScope.launch {
               viewModel.filterMyCalendarListToday()
                //viewModel.sortDescending()
            }
        }

        viewModel.myMealoneDay.observe(viewLifecycleOwner) {
            Log.d("Meal","${viewModel.myMealoneDay.value}")
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

            binding.textViewMondayDate.text = it
        }

        binding.textViewMonday.setOnClickListener {
            viewModel.mondayDate.observe(viewLifecycleOwner) {
                Log.d("Monday Date", "Montag: $it")
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                Toast.makeText(requireContext(), "Montag: $it", Toast.LENGTH_SHORT).show()
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)
                }
            }
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

                }
            }
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

                }
            }
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

                }
            }
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

                }
            }
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

                }
            }
        }

        viewModel.sundayDate.observe(viewLifecycleOwner) {
            binding.textViewSundayDate.text = it
        }

        binding.textViewSonntag.setOnClickListener {
            viewModel.sundayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Sonntag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it)
                    viewModel.filterMyCalendarList(it)

                }
            }
        }


    }
}