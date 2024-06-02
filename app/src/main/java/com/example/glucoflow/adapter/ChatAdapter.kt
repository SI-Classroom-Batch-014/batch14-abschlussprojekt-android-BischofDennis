package com.example.glucoflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glucoflow.dataOnline.modelOnline.Message
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
            //wenn der sender der eingeloggte user ist -> chatouttype
            chatOutType
        } else {
            //wenn der sender nicht der eingeloggte user ist
            chatInType
        }
    }

    inner class ChatInViewHolder(val binding: ItemChatInBinding): RecyclerView.ViewHolder(binding.root)
    inner class ChatOutViewHolder(val binding: ItemChatOutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == chatInType) {
            val binding = ItemChatInBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ChatInViewHolder(binding)
        }
        //zurück geben
        val binding = ItemChatOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatOutViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = dataset[position]

        if (holder is ChatInViewHolder) {
            //wenn die nachricht von partner kommt
            holder.binding.textViewIncoming.text = message.text
        } else if (holder is ChatOutViewHolder) {
            //wenn nachricht vom sender selbst kommt nimm für recycler view den outItem
            holder.binding.textViewOutgoing.text = message.text
        }
    }
}