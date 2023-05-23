package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.ui.dialogs.AddExerciseDialogFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.RoutineSetMainViewModel
import com.nejj.workoutorganizerapp.util.Constants
import com.nejj.workoutorganizerapp.util.Constants.Companion.ROUTINE_ARGUMENT_KEY

class AddRoutineExerciseExercisesFragment : ExercisesFragment() {

    private val args: AddRoutineExerciseExercisesFragmentArgs by navArgs()
    private val routineSetViewModel: RoutineSetMainViewModel by activityViewModels()
    private var maxOrder = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exercisesAdapter.setOnItemClickListener(exerciseClickedListener)

        viewBinding.fabAddExercise.visibility = View.GONE

        val category = args.exerciseCategory;

        exercisesViewModel.getEntities().observe(viewLifecycleOwner) { exercisesList ->
            val filteredExercises = exercisesList.filter { exercise -> exercise.category == category.name }
            exercisesAdapter.differ.submitList(filteredExercises)
        }
    }

    private val exerciseClickedListener = fun(exercise: Exercise) {
        val navHostFragment = parentFragment as NavHostFragment
        val dialogFragment = navHostFragment.parentFragment as AddExerciseDialogFragment

        val workoutRoutine = dialogFragment.arguments?.getSerializable(ROUTINE_ARGUMENT_KEY) as WorkoutRoutine

        routineSetViewModel.insertRoutineSet(workoutRoutine.routineId!!, exercise.exerciseId!!)

        dialogFragment.dismiss()
    }

    override fun hideOptionsIcon() : Boolean {
        return true
    }
}