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
import com.example.glucoflow.data.model.Profile
import com.example.glucoflow.databinding.FragmentChathomeBinding

class FragmentChatHome: Fragment() {

    private lateinit var viewBinding: FragmentChathomeBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * TODO:
     *  -   Logout
     *  - beim click auf logout button
     *      1. logout von firebase
     *      2. navigieren zum loginView
     */

    /**
     * TODO:
     *  -   1 .Zeige alle erstellten Benutzer in einer Lsite an.
     *  -   2. Aktualisiere die Lister der Benuzter bei Änderungen der Sammlung profiles
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentChathomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        setupChatList()
        setLogoutButtonOnClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupChatList() {
        viewModel.profileCollectionReference.addSnapshotListener { value, error ->
            if (value != null && error == null) {
                val profileList = value.map { it.toObject(Profile::class.java) }.toMutableList()
                profileList.removeAll { it.userId == viewModel.currentUser.value!!.uid }
                viewBinding.rvUsers.adapter = UserAdapter(profileList, viewModel)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser == null) {
                findNavController().navigate(R.id.fragmentLogin)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLogoutButtonOnClickListener() {
        viewBinding.btLogout.setOnClickListener {
            viewModel.logout()
        }
    }
}