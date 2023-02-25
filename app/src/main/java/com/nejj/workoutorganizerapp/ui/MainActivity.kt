package com.nejj.workoutorganizerapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.workoutsNavHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        //navController = findNavController(binding.workoutsNavHostFragment.id)
        //binding.bottomNavigationView.setupWithNavController(navController)
        //binding.bottomNavigationView.setupWithNavController(binding.workoutsNavHostFragment.findNavController())
    }
}