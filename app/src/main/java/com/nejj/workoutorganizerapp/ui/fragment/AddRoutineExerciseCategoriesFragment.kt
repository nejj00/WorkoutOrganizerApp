package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.models.ExerciseCategory

class AddRoutineExerciseCategoriesFragment : CategoriesFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesAdapter.setOnItemClickListener(categoryClickedListener)

        viewBinding.fabAddCategory.visibility = View.GONE

    }

    private val categoryClickedListener = fun(category: ExerciseCategory) {
        val bundle = Bundle().apply {
            putSerializable("exerciseCategory", category)
        }
        findNavController().navigate(
            R.id.action_categoriesFragment2_to_exercisesFragment2,
            bundle
        )
    }

    override fun hideOptionsIcon() : Boolean {
        return true
    }
}