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
import com.example.glucoflow.databinding.FragmentLoginBinding

@RequiresApi(Build.VERSION_CODES.O)
class FragmentLogin : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        setButtonsOnClickListener()
    }


    private fun addObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                findNavController().navigate(R.id.fragmentHome)
            }
        }
        //recaptcha token

        viewModel.authResult.observe(viewLifecycleOwner) {
            if (!it.isSuccessful){
                Toast.makeText(context, it.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setButtonsOnClickListener() {
        setButtonLoginOnClickListener()
        setButtonRegisterOnClickListener()
    }


    private fun setButtonRegisterOnClickListener() {
        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.fragmentRegister)
        }
    }

    private fun setButtonLoginOnClickListener() {
        binding.buttonLogIn.setOnClickListener {
            val email = binding.editTextTextName.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            //viewModel.getRecaptchaToken(email,password)
            viewModel.login(email, password)
        }
    }
}