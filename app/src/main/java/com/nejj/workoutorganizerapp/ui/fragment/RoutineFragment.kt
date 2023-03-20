package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.WorkoutExercisesAdapter
import com.nejj.workoutorganizerapp.databinding.ActivityRoutineBinding

class RoutineFragment : Fragment(R.layout.activity_routine) {

    private lateinit var viewBinding: ActivityRoutineBinding
    private lateinit var workoutExerciseAdapter: WorkoutExercisesAdapter
    val args: RoutineFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = ActivityRoutineBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val workoutRoutine = args.routine
        viewBinding.tiRoutineName.editText?.setText(workoutRoutine.name)
        viewBinding.tiRoutineNotes.editText?.setText(workoutRoutine.notes)

        workoutExerciseAdapter.differ.submitList(workoutRoutine.workoutExercises?.toList())
    }

    private fun setupRecyclerView() {
        workoutExerciseAdapter = WorkoutExercisesAdapter()
        viewBinding.rvRoutineExercises.apply {
            adapter = workoutExerciseAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}