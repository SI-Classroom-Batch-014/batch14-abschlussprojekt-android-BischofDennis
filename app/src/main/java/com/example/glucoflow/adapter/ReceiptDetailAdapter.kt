package com.example.glucoflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glucoflow.databinding.ReceiptdetailItemBinding
import com.example.glucoflow.ui.MealViewModel

class ReceiptDetailAdapter (
    private val dataset: List<String>,
    private val viewModel: MealViewModel
) : RecyclerView.Adapter<ReceiptDetailAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ReceiptdetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ReceiptdetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.binding.textViewIngredients.text = item
    }
}