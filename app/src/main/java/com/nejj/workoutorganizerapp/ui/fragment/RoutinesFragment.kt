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
import com.nejj.workoutorganizerapp.repositories.RoutineRepository


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

        routineAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("routine", it)
            }
            findNavController().navigate(
                R.id.action_routinesFragment_to_routineFragment,
                bundle
            )
        }

        val routineRepository = RoutineRepository()
        routineAdapter.differ.submitList(routineRepository.getWorkoutRoutines().toList())
    }

    private fun setupRecyclerView() {
        routineAdapter = RoutineAdapter()
        viewBinding.rvRoutines.apply {
            adapter = routineAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}