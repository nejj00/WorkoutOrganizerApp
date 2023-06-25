package com.nejj.workoutorganizerapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.identity.Identity
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.databinding.ActivityMainBinding
import com.nejj.workoutorganizerapp.models.LastLoggedInUser
import com.nejj.workoutorganizerapp.repositories.TestingRepository
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.sign_in.GoogleAuthUiClient
import com.nejj.workoutorganizerapp.sign_in.SignInViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var categoriesViewModel: CategoriesMainViewModel
    lateinit var exercisesViewModel: ExercisesMainViewModel
    lateinit var workoutRoutineViewModel: WorkoutRoutineMainViewModel
    lateinit var routineSetMainViewModel: RoutineSetMainViewModel
    lateinit var loggedWorkoutRoutineViewModel: LoggedWorkoutRoutineViewModel
    lateinit var loggedRoutineSetViewModel: LoggedRoutineSetViewModel
    lateinit var loggedExerciseSetViewModel: LoggedExerciseSetViewModel
    lateinit var lastLoggedInUserViewModel: LastLoggedInUserViewModel
    lateinit var statisticsViewModel: StatisticsViewModel

    private val signInViewModel = SignInViewModel()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    companion object {
        const val TAG = "MainActivity"
    }
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

        val workoutRepository = WorkoutRepository(WorkoutDatabase(this))

        val viewModelProviderFactory = BasicViewModelProviderFactory(application, workoutRepository)

        categoriesViewModel = ViewModelProvider(this, viewModelProviderFactory).get(CategoriesMainViewModel::class.java)
        exercisesViewModel = ViewModelProvider(this, viewModelProviderFactory).get(ExercisesMainViewModel::class.java)
        workoutRoutineViewModel = ViewModelProvider(this, viewModelProviderFactory).get(WorkoutRoutineMainViewModel::class.java)
        routineSetMainViewModel = ViewModelProvider(this, viewModelProviderFactory).get(RoutineSetMainViewModel::class.java)
        loggedWorkoutRoutineViewModel = ViewModelProvider(this, viewModelProviderFactory).get(LoggedWorkoutRoutineViewModel::class.java)
        loggedRoutineSetViewModel = ViewModelProvider(this, viewModelProviderFactory).get(LoggedRoutineSetViewModel::class.java)
        loggedExerciseSetViewModel = ViewModelProvider(this, viewModelProviderFactory).get(LoggedExerciseSetViewModel::class.java)
        lastLoggedInUserViewModel = ViewModelProvider(this, viewModelProviderFactory).get(LastLoggedInUserViewModel::class.java)
        statisticsViewModel = ViewModelProvider(this, viewModelProviderFactory).get(StatisticsViewModel::class.java)

        binding.drawerNavigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.categoriesFragment -> {
                    navController.navigate(R.id.categoriesFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.exercisesFragment -> {
                    navController.navigate(R.id.exercisesFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.userLogin -> {
                    if(googleAuthUiClient.getSignedInUser() != null) {
                        Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show()
                    } else {
                        loginGoogleAuth()
                        googleAuthUiClient.userUID.observe(this) {
                            lastLoggedInUserViewModel.upsertEntity(LastLoggedInUser(null, it))
                            lastLoggedInUserViewModel.updateAllEntitiesUserUID(it)
                        }
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.userLogout -> {
                    if(googleAuthUiClient.getSignedInUser() != null) {
                        logOutUser()
                    }else {
                        Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.backupLocalData -> {
                    if(googleAuthUiClient.getSignedInUser() != null) {
                        categoriesViewModel.saveExerciseCategoriesFirebase()
                        exercisesViewModel.saveExerciseCategoriesFirebase()
                        workoutRoutineViewModel.saveExerciseCategoriesFirebase()
                        routineSetMainViewModel.saveExerciseCategoriesFirebase()
                        loggedWorkoutRoutineViewModel.saveExerciseCategoriesFirebase()
                        loggedRoutineSetViewModel.saveExerciseCategoriesFirebase()
                        loggedExerciseSetViewModel.saveExerciseCategoriesFirebase()
                    }else {
                        Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.deleteBackup -> {
                    if(googleAuthUiClient.getSignedInUser() != null) {
                        categoriesViewModel.deleteEntitiesFirebase()
                        exercisesViewModel.deleteEntitiesFirebase()
                        workoutRoutineViewModel.deleteEntitiesFirebase()
                        routineSetMainViewModel.deleteEntitiesFirebase()
                        loggedWorkoutRoutineViewModel.deleteEntitiesFirebase()
                        loggedRoutineSetViewModel.deleteEntitiesFirebase()
                        loggedExerciseSetViewModel.deleteEntitiesFirebase()
                    }else {
                        Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {
                    R.id.routinesFragment, R.id.workoutLogFragment, R.id.statisticsFragment ->
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    else -> binding.bottomNavigationView.visibility = View.GONE
                }
            }

        //initializeData()
    }

    private fun logOutUser() {
        lifecycleScope.launch {
            googleAuthUiClient.signOut()
            Toast.makeText(this@MainActivity, "Logged out", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
        ActivityResultCallback { result ->
            if(result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    signInViewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    private fun loginGoogleAuth() = lifecycleScope.launch {
        val signInIntentSender = googleAuthUiClient.signIn()
        launcher.launch(
            IntentSenderRequest.Builder(
                signInIntentSender ?: return@launch
            ).build()
        )
    }

    private fun initializeData() {
        val testingRepository = TestingRepository()
        testingRepository.getCategories().forEach { categoriesViewModel.insertEntity(it) }
        testingRepository.getExercises().forEach { exercisesViewModel.insertEntity(it) }
        testingRepository.getWorkoutRoutines().forEach { workoutRoutineViewModel.insertEntity(it) }
        testingRepository.getRoutineSets().forEach { routineSetMainViewModel.insertEntity(it) }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}