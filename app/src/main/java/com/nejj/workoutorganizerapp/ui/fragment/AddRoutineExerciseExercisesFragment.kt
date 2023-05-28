package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.nejj.workoutorganizerapp.enums.AddExerciseDialogContext
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.ui.dialogs.AddExerciseDialogFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedRoutineSetViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.RoutineSetMainViewModel
import com.nejj.workoutorganizerapp.util.Constants
import com.nejj.workoutorganizerapp.util.Constants.Companion.ROUTINE_ARGUMENT_KEY

class AddRoutineExerciseExercisesFragment : ExercisesFragment() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var dialogFragment: AddExerciseDialogFragment

    private val args: AddRoutineExerciseExercisesFragmentArgs by navArgs()
    private val routineSetViewModel: RoutineSetMainViewModel by activityViewModels()
    private val loggedRoutineSetViewModel: LoggedRoutineSetViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navHostFragment = parentFragment as NavHostFragment
        dialogFragment = navHostFragment.parentFragment as AddExerciseDialogFragment

        val context = dialogFragment.arguments?.getSerializable("addDialogContext")

        when(context) {
            AddExerciseDialogContext.ADD_ROUTINE_SET ->
                exercisesAdapter.setOnItemClickListener(exerciseClickedListenerAdd)
            AddExerciseDialogContext.EDIT_ROUTINE_SET ->
                exercisesAdapter.setOnItemClickListener(exerciseClickedListenerEdit)
            AddExerciseDialogContext.ADD_LOGGED_ROUTINE_SET ->
                exercisesAdapter.setOnItemClickListener(exerciseClickedListenerAddLogged)
        }



        viewBinding.fabAddExercise.visibility = View.GONE

        val category = args.exerciseCategory;

        exercisesViewModel.getEntities().observe(viewLifecycleOwner) { exercisesList ->
            val filteredExercises = exercisesList.filter { exercise -> exercise.category == category.name }
            exercisesAdapter.differ.submitList(filteredExercises)
        }
    }

    private val exerciseClickedListenerAdd = fun(exercise: Exercise) {
        val workoutRoutine = dialogFragment.arguments?.getSerializable(ROUTINE_ARGUMENT_KEY) as WorkoutRoutine

        routineSetViewModel.insertRoutineSet(workoutRoutine.routineId!!, exercise.exerciseId!!)

        dialogFragment.dismiss()
    }

    private val exerciseClickedListenerEdit = fun(exercise: Exercise) {
        val routineSet = dialogFragment.arguments?.getSerializable("routineSet") as RoutineSet

        routineSet.exerciseId = exercise.exerciseId!!
        routineSetViewModel.insertEntity(routineSet)

        dialogFragment.dismiss()
    }

    private val exerciseClickedListenerAddLogged = fun(exercise: Exercise) {
        val loggedWorkoutRoutine = dialogFragment.arguments?.getSerializable("loggedWorkoutRoutine") as LoggedWorkoutRoutine

        loggedRoutineSetViewModel.insertNewLoggedRoutineSet(loggedWorkoutRoutine.loggedRoutineId!!, exercise)

        dialogFragment.dismiss()
    }

    override fun hideOptionsIcon() : Boolean {
        return true
    }
}