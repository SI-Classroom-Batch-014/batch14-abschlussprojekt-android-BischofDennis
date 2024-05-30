package com.example.glucoflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.R
import com.example.glucoflow.data.model.Meal
import com.example.glucoflow.databinding.GlucosedayItemBinding
import com.example.glucoflow.databinding.ReceiptItemBinding
import com.example.glucoflow.db.model.Glucose
import com.example.glucoflow.ui.MealViewModel

class GlucoseAdapter(
    private val dataset: MutableList<Glucose>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<GlucoseAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: GlucosedayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = GlucosedayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        //recyclerview für den Glucosewidget
        holder.binding.textViewGlucoseWert.text = item.glucosevalue
        holder.binding.textViewGlucoseWertUhrzeit.text = item.dateTime

    }
}