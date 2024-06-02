package com.example.glucoflow.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.dataOnline.modelOnline.Profile
import com.example.glucoflow.databinding.ItemUserBinding

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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val profileItem = dataset[position]
        holder.binding.tvUsername.text = profileItem.username

        //Auf User Chat klicken übergabe anstatt argument ein LiveData
        holder.binding.cvUser.setOnClickListener {
            viewModel.setCurrentChat(profileItem.userId)
            viewModel.setChatPartnerName(profileItem.username)
            holder.itemView.findNavController().navigate(R.id.fragmentChat)
        }

    }
}