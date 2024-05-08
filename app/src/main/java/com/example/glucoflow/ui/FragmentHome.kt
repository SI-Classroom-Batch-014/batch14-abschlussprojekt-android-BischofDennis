package com.example.glucoflow.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.adapter.GlucoseAdapter
import com.example.glucoflow.adapter.ReceiptAdapter
import com.example.glucoflow.databinding.FragmentHomeBinding
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentHome: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGlucoseList()
        viewModel.filterGlucoseList("2024-05-08")
        val calendar = Calendar.getInstance()
        val dateTimeInput = SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault()).format(calendar.time)

        viewModel.glucoseListoneDay.observe(viewLifecycleOwner){
            Log.d("Glucose","${viewModel.glucoseListoneDay.value}")
            binding.rvGlucose.adapter = GlucoseAdapter(it,viewModel)
            notify()

        }


    }

}