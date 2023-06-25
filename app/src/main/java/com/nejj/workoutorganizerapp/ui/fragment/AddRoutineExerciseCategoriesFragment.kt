package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.models.ExerciseCategory

class AddRoutineExerciseCategoriesFragment : CategoriesFragment() {

    private val args: AddRoutineExerciseCategoriesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesAdapter.setOnItemClickListener(categoryClickedListener)

        viewBinding.fabAddCategory.visibility = View.GONE

    }

    private val categoryClickedListener = fun(category: ExerciseCategory) {
        val bundle = Bundle().apply {
            putSerializable("exerciseCategory", category)
            putSerializable("entityId", args.entityId)
            putSerializable("fragmentContext", args.fragmentContext)
        }
        findNavController().navigate(
            R.id.action_addRoutineExerciseCategoriesFragment_to_addRoutineExerciseExercisesFragment,
            bundle
        )
    }

    override fun hideOptionsIcon() : Boolean {
        return true
    }
}