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
import com.example.glucoflow.databinding.FragmentProfileBinding

@RequiresApi(Build.VERSION_CODES.O)
class FragmentProfile: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }


    fun setOnClickListener(){

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.fragmentLoginSignup)
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.fragmentHome)
        }

        //Aktuell eingeloggter User
        viewModel.currentUser.observe(viewLifecycleOwner){
            if (it != null) {
                binding.textViewEmailAdress.text = it.email
            }
        }

    }


}