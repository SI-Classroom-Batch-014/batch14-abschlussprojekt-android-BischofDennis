package com.example.glucoflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.glucoflow.adapter.ReceiptAdapter
import com.example.glucoflow.databinding.FragmentHomeBinding
import com.example.glucoflow.databinding.FragmentReceiptBinding

class FragmentReceipt: Fragment() {

    private lateinit var binding: FragmentReceiptBinding
    private val viewModel: MealViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiptBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button1.setOnClickListener{
            viewModel.loadMeal(binding.searchview.text.toString())
        }

        // Verbesserte Performance bei fixer Listengröße
        binding.recyclerViewMeal.setHasFixedSize(true)


        viewModel.randomMeal.observe(viewLifecycleOwner){
            binding.recyclerViewMeal.adapter = ReceiptAdapter(it,viewModel)
        }
    }
}