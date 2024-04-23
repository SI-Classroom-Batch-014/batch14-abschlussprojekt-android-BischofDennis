package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.glucoflow.R
import com.example.glucoflow.databinding.FragmentProfilabfrageBinding

class FragmentProfilabfrage: Fragment() {

    private lateinit var binding: FragmentProfilabfrageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilabfrageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewClickableWeiter.setOnClickListener{
            findNavController().navigate(R.id.fragmentHome)
        }
    }
}