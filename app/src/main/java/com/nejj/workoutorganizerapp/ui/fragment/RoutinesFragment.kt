package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.RoutineAdapter
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.ui.viewmodels.WorkoutRoutineMainViewModel

class RoutinesFragment : ItemsListViewFragment<WorkoutRoutine>() {

    private val workoutRoutineViewModel: WorkoutRoutineMainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutRoutineViewModel.getEntities().observe(viewLifecycleOwner) { routines ->
            simpleItemPreviewAdapter.differ.submitList(routines)
        }
    }

    override val itemClickedListener = fun(routine: WorkoutRoutine) {
        val bundle = Bundle().apply {
            putSerializable("routine", routine)
        }
        findNavController().navigate(
            R.id.action_routinesFragment_to_routineFragment,
            bundle
        )
    }

    override val addItemListener = fun(_: View) {
        val bundle = Bundle().apply {
            putSerializable("routine", WorkoutRoutine())
        }
        findNavController().navigate(
            R.id.action_routinesFragment_to_routineFragment,
            bundle
        )
    }

    override fun getAdapter(): SimpleItemPreviewAdapter<WorkoutRoutine> {
        return RoutineAdapter()
    }
}