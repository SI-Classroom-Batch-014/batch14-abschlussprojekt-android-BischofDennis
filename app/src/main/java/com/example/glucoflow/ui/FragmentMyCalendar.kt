package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.databinding.FragmentCalendarBinding
import com.example.glucoflow.db.model.MyCalendar
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**  viewModel.insertCalendar(MyCalendar(
            0,
            "Start",
            "02.02.2011",
            "3:33"
        ))*/

        //Todo funktioniert nicht wenn ich kein Datum,heutiges datum muss ich 2 mal klicken
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
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = kalendarInput

            /** Verwende das heutige Datum, wenn kein Datum ausgewählt wurde
            if (formattedDate.isEmpty()) {
                val calendar = Calendar.getInstance()
                formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
            }*/

            viewModel.insertCalendar(
                MyCalendar(
                    title = title,
                    date = formattedDate,
                    time = time,
                    haufigkeit = haufikgkeit
                )
            )

            //zurücksetzen
            binding.editTextTitle.text.clear()
            binding.editTextTime.text.clear()
            binding.editTextTimeHaufigkeitanzahl.text.clear()
        }
    }
}