package com.example.glucoflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.glucoflow.R
import com.example.glucoflow.dataOnline.modelOnline.Meal
import com.example.glucoflow.databinding.ReceiptItemBinding
import com.example.glucoflow.ui.MealViewModel

class ReceiptAdapter (
    private val dataset: List<Meal>,
    private val viewModel: MealViewModel
    ) : RecyclerView.Adapter<ReceiptAdapter.ItemViewHolder>() {

        inner class ItemViewHolder(val binding: ReceiptItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding = ReceiptItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return dataset.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = dataset[position]
            holder.binding.receiptViewMealImage.load(item.strMealThumb) {
                crossfade(true)
                crossfade(500) // Animation für das Einblenden in Millisekunden
                placeholder(R.drawable.ic_launcher_foreground) // Platzhalter-Bild, während das Bild geladen wird
                error(R.drawable.ic_launcher_background) // Bild, das angezeigt wird, falls ein Fehler auftritt
            }

            holder.binding.textViewMealName.text = item.strMeal

            holder.binding.root.setOnClickListener{

                viewModel.setCurrentMeal(item)
                //Einzeln aufrufen, weil der Category Call ist anders aufgebaut (3 Eigenschaften) als der normale Call
                viewModel.getMealbyId(item.id)
                holder.itemView.findNavController().navigate(R.id.fragmentReceiptDetail)
            }

        }
    }