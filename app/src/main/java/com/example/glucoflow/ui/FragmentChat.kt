package com.example.glucoflow.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.adapter.ChatAdapter
import com.example.glucoflow.dataOnline.modelOnline.Chat
import com.example.glucoflow.databinding.FragmentChatBinding

@RequiresApi(Build.VERSION_CODES.O)
class FragmentChat: Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: MainViewModel by activityViewModels()

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
        setupChatMessages()
        setButtonSendOnClickListener()

        //ChatPartner Username
        viewModel.chatPartner.observe(viewLifecycleOwner){
            binding.textViewUsername.text = it.toString()
        }

    }


    //Der richtige Chat aus der Liste wird eingestellt und
    // die richtige ChatMessage wird zum Adapter übergeben
    private fun setupChatMessages() {
        viewModel.currentChatDocumentReference.addSnapshotListener { value, error ->
            if (value != null && error == null) {

                val chat = value.toObject(Chat::class.java)
                Log.d("ChatAdapter","$chat")
                val chatMessages = chat?.messages
                Log.d("ChatAdapter","$chatMessages")
                binding.rvChatMessages.adapter = chatMessages?.let { ChatAdapter(it, viewModel.currentUser.value!!.uid) }
            }
        }
    }

    //Nachricht abschicken
    private fun setButtonSendOnClickListener() {
        binding.btSend.setOnClickListener {
            val message = binding.tietMessage.text.toString()
            viewModel.sendMessage(message)
            binding.tietMessage.text?.clear()
        }

        binding.buttonBack.setOnClickListener(){
            findNavController().navigate(R.id.fragmentChatHome)
        }
    }

}