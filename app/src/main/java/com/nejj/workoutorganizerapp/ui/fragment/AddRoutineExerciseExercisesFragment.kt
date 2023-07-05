package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.enums.FragmentContext
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedRoutineSetViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.RoutineSetMainViewModel
import kotlinx.coroutines.launch
import java.util.*

class AddRoutineExerciseExercisesFragment : ExercisesFragment() {

    private val args: AddRoutineExerciseExercisesFragmentArgs by navArgs()
    private val routineSetViewModel: RoutineSetMainViewModel by activityViewModels()
    private val loggedRoutineSetViewModel: LoggedRoutineSetViewModel by activityViewModels()
    val exerciseList = mutableListOf<Exercise>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(args.fragmentContext) {
            FragmentContext.ROUTINE_CONTEXT ->
                exercisesAdapter.setOnItemClickListener(exerciseClickedListenerAdd)
            FragmentContext.EDIT_ROUTINE_SET_CONTEXT ->
                exercisesAdapter.setOnItemClickListener(exerciseClickedListenerEdit)
            FragmentContext.WORKOUT_CONTEXT ->
                exercisesAdapter.setOnItemClickListener(exerciseClickedListenerAddLogged)
        }

        viewBinding.fabAddExercise.visibility = View.GONE

        val category = args.exerciseCategory;

        exercisesViewModel.getEntities().observe(viewLifecycleOwner) { exercisesList ->
            val filteredExercises = exercisesList.filter { exercise -> exercise.categoryId == category.categoryId }
            exerciseList.addAll(filteredExercises)
            exercisesAdapter.differ.submitList(filteredExercises)
        }

        setUpAppBarMenuItems(view)
    }

    private val exerciseClickedListenerAdd = fun(exercise: Exercise) {
        val workoutRoutineId = args.entityId

        routineSetViewModel.insertRoutineSet(workoutRoutineId, exercise.exerciseId!!)

        findNavController().popBackStack(R.id.routineFragment, false)
    }

    private val exerciseClickedListenerEdit = fun(exercise: Exercise) {
        val routineSetId = args.entityId

        lifecycleScope.launch {
            val routineSet = routineSetViewModel.getRoutineSetsById(routineSetId)
            routineSet.exerciseId = exercise.exerciseId!!
            routineSetViewModel.insertEntity(routineSet)
            findNavController().popBackStack(R.id.routineFragment, false)
        }
    }

    private val exerciseClickedListenerAddLogged = fun(exercise: Exercise) {
        val loggedWorkoutRoutineId = args.entityId

        loggedRoutineSetViewModel.insertNewLoggedRoutineSet(loggedWorkoutRoutineId, exercise)

        findNavController().popBackStack(R.id.workoutFragment, false)
    }

    override fun hideOptionsIcon() : Boolean {
        return true
    }

    private fun setUpAppBarMenuItems(view: View) {
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_exercise_toolbar, menu)
                val searchItem = menu.findItem(R.id.searchExercise)
                val searchView = searchItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        exerciseList.filter { exercise ->
                            exercise.name.lowercase(Locale.ROOT).contains(newText.toString().lowercase(), true)
                        }.let {
                            exercisesAdapter.differ.submitList(it)
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.addExercise -> {
                        return true
                    }
                }
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}