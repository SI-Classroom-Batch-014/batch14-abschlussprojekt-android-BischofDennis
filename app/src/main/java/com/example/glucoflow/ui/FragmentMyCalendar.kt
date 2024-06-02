package com.example.glucoflow.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.dataOnline.modelOnline.CalendarFirebase
import com.example.glucoflow.databinding.FragmentCalendarBinding
import com.example.glucoflow.dataRoom.model.MyCalendar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FragmentMyCalendar : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: MainViewModel by activityViewModels()
    lateinit var formattedDate: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Initialisiere formattedDate mit dem aktuellen Datum
         *         damit das heutige datum genommen wird,
         *         falls man kein auf dem Kalendar auswählt
         */
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        formattedDate = dateFormat.format(calendar.time)


        /**  viewModel.insertCalendar(MyCalendar(
            0,
            "Start",
            "02.02.2011",
            "3:33"
        ))*/
        binding.profilePicture.setOnClickListener {
            findNavController().navigate(R.id.fragmentProfile)
        }

        binding.calendarView3.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val formattedMonth = String.format("%02d", month + 1) // Stelle sicher, dass der Monat zweistellig ist
            formattedDate = String.format("%02d", dayOfMonth) + "." + formattedMonth + "." + year
        }

        binding.buttonSave.setOnClickListener {
            val kalendarInput = binding.calendarView3.date
            val title = binding.editTextTitle.text.toString()
            val time = binding.editTextTime.text.toString()
            val haufikgkeit = binding.editTextTimeHaufigkeitanzahl.text.toString()


            //von Kalender aktuelle Zeit
            calendar.timeInMillis = kalendarInput

            val calender = MyCalendar(
                title = title,
                date = formattedDate,
                time = time,
                haufigkeit = haufikgkeit
            )

            viewModel.insertCalendar(
               calender
            )

            val calenderFirebase = CalendarFirebase(
                title = title,
                date = formattedDate,
                time = time,
                haufigkeit = haufikgkeit
            )
            viewModel.setCalendarOnline(
                calenderFirebase
            )

            //zurücksetzen
            binding.editTextTitle.text.clear()
            binding.editTextTime.text.clear()
            binding.editTextTimeHaufigkeitanzahl.text.clear()
        }
    }
}