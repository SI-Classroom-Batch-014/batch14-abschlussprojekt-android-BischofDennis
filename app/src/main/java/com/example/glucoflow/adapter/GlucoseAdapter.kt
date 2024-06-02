package com.example.glucoflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.dataOnline.modelOnline.GlucoseFirebase
import com.example.glucoflow.databinding.GlucosedayItemBinding
import com.example.glucoflow.dataRoom.model.Glucose

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