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
import com.example.glucoflow.dataOnline.modelOnline.GlucoseFirebase
import com.example.glucoflow.databinding.FragmentGlucoseBinding
import com.example.glucoflow.dataRoom.model.Glucose
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profilePicture.setOnClickListener {
            findNavController().navigate(R.id.fragmentProfile)
        }

        //von Kalender aktuelle Zeit
        val calendar = Calendar.getInstance()
        val dateTimeInput = SimpleDateFormat("dd.MM.yyyy HH:mm:ss",
            Locale.getDefault()).format(calendar.time)

        //Glucose Wert mit akutellen Datum gespeichert
        binding.textViewButtonSpeichern.setOnClickListener {
            val glucoseInput =  binding.editTextTextGlucoseInput.text.toString()
            val carbonHydrate = binding.editTextTextKohlenhydrateInput.text.toString()

            val glucose =  Glucose(
                glucosevalue =glucoseInput + "mg/dl",
                dateTime = dateTimeInput,
                carbon = carbonHydrate
            )

            viewModel.insertGlucose(
              glucose
            )

            val glucoseFirebase = GlucoseFirebase(
                id = "",
                glucosevalue = glucoseInput,
                dateTime = dateTimeInput,
                carbon = carbonHydrate
            )

            viewModel.setGlucoseOnline(glucoseFirebase)
            //EditTextFeld zurücksetzen
            binding.editTextTextGlucoseInput.text.clear()
            binding.editTextTextKohlenhydrateInput.text.clear()
        }
        //von Kalender aktuelle Zeit
        binding.textViewDatumUhrzeit.text = dateTimeInput


    }
}