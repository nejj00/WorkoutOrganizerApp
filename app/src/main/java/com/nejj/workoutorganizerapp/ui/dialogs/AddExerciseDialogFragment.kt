package com.nejj.workoutorganizerapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.DialogAddExerciseBinding
import com.nejj.workoutorganizerapp.models.Exercise

class AddExerciseDialogFragment : DialogFragment() {

    private lateinit var viewBinding: DialogAddExerciseBinding
    private lateinit var navController: NavController

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DialogAddExerciseBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(viewBinding.addExerciseNavHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf()
        )
        viewBinding.tabAddRoutineExercise.setupWithNavController(navController, appBarConfiguration)
        viewBinding.tabAddRoutineExercise.setNavigationIcon(R.drawable.ic_close)

        navController.addOnDestinationChangedListener() { _, destination, _ ->
            when(destination.id) {
                navController.graph.startDestinationId -> {
                    viewBinding.tabAddRoutineExercise.setNavigationIcon(R.drawable.ic_close)
                }
            }
        }

        viewBinding.tabAddRoutineExercise.setNavigationOnClickListener {
            if(navController.currentDestination!!.id == navController.graph.startDestinationId) {
                dismiss()
            } else {
                navController.navigateUp()
            }
        }
    }

}