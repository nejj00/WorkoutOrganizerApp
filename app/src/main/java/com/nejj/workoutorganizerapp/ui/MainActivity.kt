package com.nejj.workoutorganizerapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.databinding.ActivityMainBinding
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.ui.viewmodels.BasicViewModelProviderFactory
import com.nejj.workoutorganizerapp.ui.viewmodels.CategoriesMainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var viewModel: CategoriesMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.workoutsNavHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.routinesFragment, R.id.workoutLogFragment, R.id.statisticsFragment),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.abMainAppBar.setupWithNavController(navController, appBarConfiguration)

        binding.drawerNavigationView.setupWithNavController(navController)

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {
                    R.id.routinesFragment, R.id.workoutLogFragment, R.id.statisticsFragment ->
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    else -> binding.bottomNavigationView.visibility = View.GONE
                }
            }

        val workoutRepository = WorkoutRepository(WorkoutDatabase(this))
        val viewModelProviderFactory = BasicViewModelProviderFactory(application, workoutRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(CategoriesMainViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}