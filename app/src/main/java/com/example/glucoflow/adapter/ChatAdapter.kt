package com.example.glucoflow.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glucoflow.data.model.Message
import com.example.glucoflow.databinding.ItemChatInBinding
import com.example.glucoflow.databinding.ItemChatOutBinding

class ChatAdapter(
    private val dataset: List<Message>,
    private val currentUserId: String
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val chatInType = 1
    private val chatOutType = 2

    override fun getItemViewType(position: Int): Int {
        val chatItem = dataset[position]
        return if (chatItem.sender == currentUserId) {
            chatOutType
        } else {
            chatInType
        }
    }

    inner class ChatInViewHolder(val binding: ItemChatInBinding): RecyclerView.ViewHolder(binding.root)
    inner class ChatOutViewHolder(val binding: ItemChatOutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}