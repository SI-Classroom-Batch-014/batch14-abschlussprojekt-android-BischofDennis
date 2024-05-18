package com.example.glucoflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.data.model.Profile
import com.example.glucoflow.databinding.ItemUserBinding
import com.example.glucoflow.ui.FragmentChatHomeDirections

class UserAdapter(
    private val dataset: List<Profile>,
    private val viewModel: MainViewModel
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val profileItem = dataset[position]
        holder.binding.tvUsername.text = profileItem.username


        holder.binding.root.setOnClickListener {
            //username wird übergeben
            val action = FragmentChatHomeDirections.actionFragmentChatHomeToFragmentChat(profileItem.username)
            holder.itemView.findNavController().navigate(action)
            // holder.itemView.findNavController().navigate(R.id.fragmentChat)
        }
    }
}