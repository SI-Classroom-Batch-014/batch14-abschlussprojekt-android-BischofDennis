package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.databinding.FragmentGlucoseBinding
import com.example.glucoflow.db.model.Glucose
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentGlucose: Fragment() {

    private lateinit var binding: FragmentGlucoseBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGlucoseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Glucose Wert mit akutellen Datum gespeichert
        binding.textViewButtonSpeichern.setOnClickListener {
            val glucoseInput =  binding.editTextTextGlucoseInput.text.toString()
            val carbonHydrate = binding.editTextTextKohlenhydrateInput.text.toString()


            //von Kalender aktuelle Zeit
            val calendar = Calendar.getInstance()
            val dateTimeInput = SimpleDateFormat("dd.MM.yyyy HH:mm:ss",
                Locale.getDefault()).format(calendar.time)

            viewModel.insertGlucose(
                Glucose(
                    glucosevalue =glucoseInput + "mg/dl",
                    dateTime = dateTimeInput,
                    carbon = carbonHydrate
                )
            )
            //EditTextFeld zurücksetzen
            binding.editTextTextGlucoseInput.text.clear()
            binding.editTextTextKohlenhydrateInput.text.clear()
        }
    }
}