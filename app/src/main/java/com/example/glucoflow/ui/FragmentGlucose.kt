package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.glucoflow.databinding.FragmentGlucoseBinding
import com.example.glucoflow.databinding.FragmentHomeBinding
import java.time.LocalDateTime

class FragmentGlucose: Fragment() {

    private lateinit var binding: FragmentGlucoseBinding

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
        //speicehrn button ist so lange nicht klickbar

        binding.textViewButtonSpeichern.setOnClickListener{
            val glucoseInput = binding.editTextTextGlucoseInput.text.toString()
            //val datumUhrzeit = LocalDateTime.now().hour.toString()
            //if else wenns leer ist

        }

        binding.editTextTextGlucoseInput.setOnClickListener{

        }
    }
}