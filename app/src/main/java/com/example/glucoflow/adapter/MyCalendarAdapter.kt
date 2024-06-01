package com.example.glucoflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.databinding.MycalendardayItemBinding
import com.example.glucoflow.dataRoom.model.MyCalendar

class MyCalendarAdapter(
    private val dataset: MutableList<MyCalendar>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<MyCalendarAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: MycalendardayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            MycalendardayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.binding.textViewBlutzucker.text = item.title
        holder.binding.textViewUhrzeit.text = item.time
        holder.binding.textViewHaufigkeit.text = item.haufigkeit
        holder.binding.textViewDatum.text = item.date
    }

}