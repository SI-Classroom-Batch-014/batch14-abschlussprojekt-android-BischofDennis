package com.example.glucoflow.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.example.glucoflow.MainViewModel
import com.example.glucoflow.adapter.GlucoseAdapter
import com.example.glucoflow.adapter.ReceiptAdapter
import com.example.glucoflow.databinding.FragmentHomeBinding
import com.example.glucoflow.db.GlucoseRepository
import com.example.glucoflow.db.model.Glucose
import kotlinx.coroutines.launch
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

        // viewModel.insertGlucose(Glucose(0, "test", "2024-05-08"))

        viewModel.currentDate.observe(viewLifecycleOwner){

            binding.textViewTodayDate.text = it
        }

        binding.textViewTodayDate.setOnClickListener {
            viewModel.currentDate.observe(viewLifecycleOwner){
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it) }
            }
        }


        viewModel.mondayDate.observe(viewLifecycleOwner){

            binding.textViewMondayDate.text = it
        }

        binding.textViewMonday.setOnClickListener {
            viewModel.mondayDate.observe(viewLifecycleOwner) {
                Log.d("Monday Date", "Montag: $it")
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                Toast.makeText(requireContext(), "Montag: $it", Toast.LENGTH_SHORT).show()
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it) }
            }
        }

        viewModel.tuesdayDate.observe(viewLifecycleOwner){

            binding.textViewTuesdayDate.text = it
        }

        binding.textViewDienstag.setOnClickListener {
            viewModel.tuesdayDate.observe(viewLifecycleOwner) {
                Log.d("Tuesday Date", "Dienstag: $it")
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                Toast.makeText(requireContext(), "Dienstag: $it", Toast.LENGTH_SHORT).show()
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it) }
            }
        }


        viewModel.wednesdayDate.observe(viewLifecycleOwner){
            binding.textViewMittwochDate.text = it
        }

        binding.textViewMittwoch.setOnClickListener {
            viewModel.wednesdayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Mittwoch: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it) }
            }
        }

        viewModel.thursdayDate.observe(viewLifecycleOwner){
            binding.textViewDonnerstagDate.text = it
        }

        binding.textViewDonnerstag.setOnClickListener {
            viewModel.thursdayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Donnerstag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it) }
            }
        }

        viewModel.fridayDate.observe(viewLifecycleOwner){
            binding.textViewFreitagDate.text = it
        }

        binding.textViewFreitag.setOnClickListener {
            viewModel.fridayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Freitag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it) }
            }
        }

        viewModel.saturdayDate.observe(viewLifecycleOwner){
            binding.textViewSamstagDate.text = it
        }

        binding.textViewSamstag.setOnClickListener {
            viewModel.saturdayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Samstag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it) }
            }
        }

        viewModel.sundayDate.observe(viewLifecycleOwner){
            binding.textViewSundayDate.text = it
        }

        binding.textViewSonntag.setOnClickListener {
            viewModel.sundayDate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Sonntag: $it", Toast.LENGTH_SHORT).show()
                //GlucoseListe nach ausgewählten Tag filter für den glucoseListOneDay zum Observen
                viewModel.viewModelScope.launch {
                    viewModel.filterGlucoseList(it) }
            }
        }



        //Glucose Widget
        viewModel.glucoseListoneDay.observe(viewLifecycleOwner){
            Log.d("Glucose","${viewModel.glucoseListoneDay.value}")
            binding.rvGlucose.adapter = GlucoseAdapter(it,viewModel)

        }

        //Kohlenhydrate Widget
        viewModel.glucoseListoneDay.observe(viewLifecycleOwner){
            viewModel.viewModelScope.launch {
                var carbonGesamt = 0
                // weil es eine Liste ist alles einzeln durchgehen und dann zusammen rechnen
                for (i in it){
                    carbonGesamt += i.carbon.toInt()
                }
                binding.textViewKohlenhydrate.text = carbonGesamt.toString()
            }

        }






    }

}