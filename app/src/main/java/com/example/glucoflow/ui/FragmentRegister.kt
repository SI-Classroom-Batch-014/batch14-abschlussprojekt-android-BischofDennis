package com.example.glucoflow.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.databinding.FragmentRegisterBinding

class FragmentRegister: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setButtonsOnClickListener()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { authResult ->
            if (authResult != null) {
                findNavController().navigate(R.id.fragmentProfilabfrage)
            }
        }

        //Recaptcha token
        viewModel.authResult.observe(viewLifecycleOwner) {
            if (!it.isSuccessful) {
                Toast.makeText(context, it.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setButtonsOnClickListener() {
        setRegisterButtonOnClickListener()
        setBackButtonOnClickListener()
    }

    private fun setBackButtonOnClickListener() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.fragmentLoginSignup)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setRegisterButtonOnClickListener() {
        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextTextName.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            val username = binding.editTextTextUsername.text.toString()

            //viewModel.getRecaptchaToken(email,password, username)
            viewModel.register(email, password, username)
        }
    }
}