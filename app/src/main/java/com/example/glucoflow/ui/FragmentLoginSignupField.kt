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
import kotlin.math.log

class FragmentLoginSignupField: Fragment() {

    private lateinit var binding: FragmentLoginSignupFieldBinding
    private val viewModel: MainViewModel by activityViewModels()

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

        setButtonsOnClickListener()
    }

    private fun setButtonsOnClickListener() {
        setRegisterButtonOnClickListener()
        setButtonLoginOnClickListener()
        setupObservers()
        addObservers()
        //setBackButtonOnClickListener()
    }

    private fun setRegisterButtonOnClickListener() {
        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextTextName.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            val username = binding.editTextTextUsername.text.toString()

            viewModel.register(email, password, username)
        }
    }

    private fun setButtonLoginOnClickListener() {
        binding.buttonLogIn.setOnClickListener {
            val email = binding.editTextTextName.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            viewModel.login(email, password)
        }
    }
    private fun setupObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { authResult ->
            if (authResult != null) {
                findNavController().navigate(R.id.fragmentProfilabfrage)
            }
        }
    }

    private fun addObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                findNavController().navigate(R.id.fragmentProfilabfrage)
            }
        }
    }

    /**private fun setButtonRegisterOnClickListener() {
        binding.btToRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }

    private fun setBackButtonOnClickListener() {
        binding.btBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }*/


}