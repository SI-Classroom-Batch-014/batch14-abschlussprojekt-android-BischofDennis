package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.databinding.FragmentChatBinding

class FragmentChat: Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: MainViewModel by activityViewModels()
    //übergebenen Wert mit Safe Args erhalten
    private val args: FragmentChatArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = args.username
        binding.textViewUsername.text = username
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.fragmentChatHome)
        }

    }
}