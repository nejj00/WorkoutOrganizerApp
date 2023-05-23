package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.models.Exercise

class ModifyExercisesFragment : ExercisesFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exercisesAdapter.setOnItemClickListener(exerciseClickedListener)
        exercisesAdapter.setOnOptionsClickListener(onOptionsClickedListener)

        exercisesViewModel.getEntities().observe(viewLifecycleOwner) { exercisesList ->
            exercisesAdapter.differ.submitList(exercisesList)
        }

        viewBinding.fabAddExercise.setOnClickListener(addExerciseListener)
    }

    private val exerciseClickedListener = fun(exercise: Exercise) {
        val bundle = Bundle().apply {
            putSerializable("exercise", exercise)
        }
        findNavController().navigate(
            R.id.action_exercisesFragment_to_editExerciseFragment,
            bundle
        )
    }

    private val addExerciseListener = fun(_: View) {
        val bundle = Bundle().apply {
            putSerializable("exercise", Exercise())
        }
        findNavController().navigate(
            R.id.action_exercisesFragment_to_editExerciseFragment,
            bundle
        )
    }

    private val onOptionsClickedListener = fun(exercise: Exercise, optionsView: View) {
        val popupMenu = PopupMenu(activity, optionsView, Gravity.BOTTOM)
        popupMenu.inflate(R.menu.categories_bottom_popup_menu)

        popupMenu.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.editCategory -> {
                    exerciseClickedListener(exercise)
                    return@setOnMenuItemClickListener true
                }
                R.id.deleteCategory -> {
                    exercisesViewModel.deleteEntity(exercise)
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

        popupMenu.show()
    }
}