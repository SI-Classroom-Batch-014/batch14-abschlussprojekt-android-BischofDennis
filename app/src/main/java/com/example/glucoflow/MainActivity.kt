package com.example.glucoflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.glucoflow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Nav-Host Controller
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)

        //Bottom-Navigation
        navHost.navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id) {
                R.id.fragmentLoginSignup -> binding.bottomNavigationView.visibility = View.GONE
                R.id.fragmentLogin -> binding.bottomNavigationView.visibility = View.GONE
                R.id.fragmentProfilabfrage -> binding.bottomNavigationView.visibility = View.GONE
                R.id.fragmentHome-> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.fragmentGlucose -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.fragmentReceipt -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.fragmentReceiptDetail -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.fragmentProfile-> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

    }
}