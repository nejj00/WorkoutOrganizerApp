package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.LoggedWorkoutRoutinesAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentWorkoutLogBinding
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedWorkoutRoutineViewModel

class WorkoutLogFragment : Fragment(R.layout.fragment_workout_log) {

    private lateinit var viewBinding: FragmentWorkoutLogBinding
    lateinit var loggedWorkoutRoutinesAdapter: LoggedWorkoutRoutinesAdapter
    private val loggedWorkoutRoutineViewModel: LoggedWorkoutRoutineViewModel by activityViewModels()


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

        setupRecyclerView()

        loggedWorkoutRoutinesAdapter.setOnItemClickListener(loggedWorkoutClickedListener)

        loggedWorkoutRoutineViewModel.getLoggedWorkoutRoutineWithLoggedRoutineSets().observe(viewLifecycleOwner) { loggedWorkoutRoutinesWithLoggedRoutineSets ->
            loggedWorkoutRoutinesAdapter.differ.submitList(loggedWorkoutRoutinesWithLoggedRoutineSets)
        }

        viewBinding.fabAddWorkout.setOnClickListener(addWourkoutListener)
    }

    private var loggedWorkoutClickedListener = fun(loggedWorkoutRoutineWithLoggedRoutineSets: LoggedWorkoutRoutineWithLoggedRoutineSets) {
        val bundle = Bundle().apply {
            putSerializable("loggedWorkoutRoutine", loggedWorkoutRoutineWithLoggedRoutineSets)
        }
        findNavController().navigate(
            R.id.action_workoutLogFragment_to_workoutFragment,
            bundle
        )
    }

    private var addWourkoutListener = fun(_: View) {
        val bundle = Bundle().apply {
            putSerializable("loggedWorkoutRoutine", LoggedWorkoutRoutine())
        }
        findNavController().navigate(
            R.id.action_workoutLogFragment_to_workoutFragment,
            bundle
        )
    }

    private fun setupRecyclerView() {
        loggedWorkoutRoutinesAdapter = LoggedWorkoutRoutinesAdapter()
        viewBinding.rvWorkoutLog.apply {
            adapter = loggedWorkoutRoutinesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}