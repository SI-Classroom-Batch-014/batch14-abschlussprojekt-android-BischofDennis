package com.example.glucoflow.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.adapter.ReceiptDetailAdapter
import com.example.glucoflow.dataOnline.modelOnline.MealFirebase
import com.example.glucoflow.databinding.FragmentReceiptdetailBinding
import com.example.glucoflow.dataRoom.model.Meal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentReceiptDetail: Fragment() {

    private lateinit var binding: FragmentReceiptdetailBinding
    private val viewModel: MealViewModel by activityViewModels()
    private val viewModelMain : MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiptdetailBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val randomKcal = viewModel.getRandomKcal()
        val randomKH = viewModel.getRandomKH()

        viewModel.currentMeal.observe(viewLifecycleOwner) {
            binding.textViewMealName.text = it.strMeal

            binding.receiptViewMealImageDetail.load(it.strMealThumb)

           // binding.textViewInstructionsString.text = it.strInstructions

            //binding.buttonLinkReceipt.text = it.strSource

            binding.buttonLinkReceipt.setOnClickListener {it1 ->
                it.strSource?.let {it2 -> openLinkInBrowser(it2) }
            }

           // binding.buttonYouTube.text = it.strYoutube

            binding.buttonYouTube.setOnClickListener {it1 ->
                it.strYoutube?.let {it2 ->openLinkInBrowser(it2) }
            }

            binding.buttonBack.setOnClickListener {
                findNavController().navigateUp()//oder R.id.FragmentReceipt
            }


            binding.buttonAdd.setOnClickListener {
                //von Kalender aktuelle Zeit
                val calendar = Calendar.getInstance()
                val dateTimeInput = SimpleDateFormat("dd.MM.yyyy HH:mm:ss",
                    Locale.getDefault()).format(calendar.time)

               val meal =  Meal(
                    randomKH,
                    randomKcal,
                    dateTimeInput
                )
                viewModelMain.saveKhKcal(
                    meal
                )

                val mealFirebase = MealFirebase(
                    id = "",
                    randomKH,
                    randomKcal,
                    dateTimeInput

                )
                viewModelMain.saveKhKcalOnline(
                    mealFirebase

                )
                binding.tvKcal.text = "0"
                binding.tvKH.text = "0"
            }

            // Verbesserte Performance bei fixer Listengröße
            binding.receiclerViewIngredients.setHasFixedSize(true)

            binding.scrollViewInstructionsString.text = it.strInstructions

            binding.tvKcal.text = randomKcal

            binding.tvKH.text = randomKH
        }

        //für den RecyclerView Zutaten
        viewModel.currentMealIngredients.observe(viewLifecycleOwner){
            binding.receiclerViewIngredients.adapter = ReceiptDetailAdapter(it,viewModel)
        }
    }

    private fun openLinkInBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startActivity(intent)
    }
}