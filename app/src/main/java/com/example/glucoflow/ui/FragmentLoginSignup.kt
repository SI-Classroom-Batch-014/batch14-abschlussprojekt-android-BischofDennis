package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.glucoflow.R
import com.example.glucoflow.databinding.FragmentLoginSignupBinding

class FragmentLoginSignup: Fragment() {

    private lateinit var binding: FragmentLoginSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAnmelden.setOnClickListener {
            findNavController().navigate(R.id.fragmentLogin)
        }

        binding.buttonRegistrieren.setOnClickListener {
            findNavController().navigate(R.id.fragmentRegister)
        }
    }
}