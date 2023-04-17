package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.FragmentWorkoutLogBinding
import com.nejj.workoutorganizerapp.models.WorkoutRoutine

class WorkoutLogFragment : Fragment(R.layout.fragment_workout_log) {

    private lateinit var viewBinding: FragmentWorkoutLogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentWorkoutLogBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.fabAddWorkout.setOnClickListener(addWourkoutListener)
    }

    private var addWourkoutListener = fun(_: View) {
        val bundle = Bundle().apply {
            putSerializable("workout", WorkoutRoutine())
        }
        findNavController().navigate(
            R.id.action_workoutLogFragment_to_workoutFragment,
            bundle
        )
    }
}