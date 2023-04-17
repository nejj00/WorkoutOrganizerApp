package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.RoutineAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentRoutinesBinding
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.repositories.TestingRepository


class RoutinesFragment : Fragment(R.layout.fragment_routines) {

    private lateinit var viewBinding: FragmentRoutinesBinding
    lateinit var routineAdapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRoutinesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        routineAdapter.setOnItemClickListener(routineClickedListener)

        viewBinding.fabAddRoutine.setOnClickListener(addRoutineListener)

        val testingRepository = TestingRepository()
        routineAdapter.differ.submitList(testingRepository.getWorkoutRoutines().toList())
    }

    private val routineClickedListener = fun(routine:WorkoutRoutine) {
        val bundle = Bundle().apply {
            putSerializable("routine", routine)
        }
        findNavController().navigate(
            R.id.action_routinesFragment_to_routineFragment,
            bundle
        )
    }

    private val addRoutineListener = fun(_: View) {
        val bundle = Bundle().apply {
            putSerializable("routine", WorkoutRoutine())
        }
        findNavController().navigate(
            R.id.action_routinesFragment_to_routineFragment,
            bundle
        )
    }

    private fun setupRecyclerView() {
        routineAdapter = RoutineAdapter()
        viewBinding.rvRoutines.apply {
            adapter = routineAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}