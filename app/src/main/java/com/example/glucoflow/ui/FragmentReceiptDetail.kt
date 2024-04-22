package com.example.glucoflow.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.glucoflow.adapter.ReceiptDetailAdapter
import com.example.glucoflow.databinding.FragmentReceiptdetailBinding

class FragmentReceiptDetail: Fragment() {

    private lateinit var binding: FragmentReceiptdetailBinding
    private val viewModel: MealViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiptdetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.currentMeal.observe(viewLifecycleOwner){
            binding.receiptViewMealImageDetail.load(it.strMealThumb)


        binding.button5.setOnClickListener {
           // findNavController().navigate(FragmentReceiptDirections)
        }
        // Verbesserte Performance bei fixer Listengröße
        binding.receiclerViewIngredients.setHasFixedSize(true)


        //viewModel.currentMealIngredients.observe(viewLifecycleOwner){

        binding.receiclerViewIngredients.adapter = ReceiptDetailAdapter(mutableListOf(
            "strIngr1",
            "strIngrA",
            "strIngrB",
            "strIngrC",
        ))

    }}

    private fun openLinkInBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startActivity(intent)
    }
}