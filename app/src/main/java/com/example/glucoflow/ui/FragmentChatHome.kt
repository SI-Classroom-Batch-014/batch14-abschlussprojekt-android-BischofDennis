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
import com.example.glucoflow.adapter.UserAdapter
import com.example.glucoflow.dataOnline.modelOnline.Profile
import com.example.glucoflow.databinding.FragmentChathomeBinding

@RequiresApi(Build.VERSION_CODES.O)
class FragmentChatHome: Fragment() {

    private lateinit var binding: FragmentChathomeBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChathomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        setupChatList()
        setLogoutButtonOnClickListener()

        binding.profilePicture.setOnClickListener {
            findNavController().navigate(R.id.fragmentProfile)
        }
    }

    private fun setupChatList() {
        viewModel.profileCollectionReference.addSnapshotListener { value, error ->
            if (value != null && error == null) {
                val profileList = value.map { it.toObject(Profile::class.java) }.toMutableList()
                profileList.removeAll { it.userId == viewModel.currentUser.value!!.uid }
                binding.rvUsers.adapter = UserAdapter(profileList, viewModel)
            }
        }
    }

    private fun addObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser == null) {
                findNavController().navigate(R.id.fragmentLogin)
            }
        }
    }

    private fun setLogoutButtonOnClickListener() {
        binding.btLogout.setOnClickListener {
            viewModel.logout()
        }
    }
}