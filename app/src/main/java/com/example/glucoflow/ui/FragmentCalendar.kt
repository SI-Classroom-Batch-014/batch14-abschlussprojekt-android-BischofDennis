package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.databinding.FragmentCalendarBinding
import com.example.glucoflow.databinding.FragmentGlucoseBinding
import com.example.glucoflow.db.model.Glucose
import com.example.glucoflow.db.model.MyCalendar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentCalendar: Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: MainViewModel by activityViewModels()

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

        binding.buttonSave.setOnClickListener {
            val kalendarInput =  binding.calendarView3.date
            val title = binding.editTextTitle.text.toString()

            //von Kalender aktuelle Zeit
            val calendar = Calendar.getInstance()
            val dateTimeInput = SimpleDateFormat("dd.MM.yyyy",
                Locale.getDefault()).format(kalendarInput)

            viewModel.insertCalendar(
                MyCalendar(
                    title = title ,
                    date = dateTimeInput,
                    time = "11:11"
                )
            )

        }

    }
}