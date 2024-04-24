package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.databinding.FragmentLoginSignupFieldBinding

class FragmentLoginSignupField: Fragment() {

    private lateinit var binding: FragmentLoginSignupFieldBinding
    private val viewBinding: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginSignupFieldBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogIn.setOnClickListener {
            viewBinding.loginWithEmailAndPassword(binding.editTextTextName.text.toString(),binding.editTextTextPassword.text.toString()){
                findNavController().navigate(R.id.fragmentProfilabfrage)
            }
        }

        binding.buttonSignUp.setOnClickListener {
            viewBinding.registerWithEmailAndPassword(binding.editTextTextName.text.toString(),binding.editTextTextPassword.text.toString()){
                findNavController().navigate(R.id.fragmentProfilabfrage)
            }
        }
    }
}