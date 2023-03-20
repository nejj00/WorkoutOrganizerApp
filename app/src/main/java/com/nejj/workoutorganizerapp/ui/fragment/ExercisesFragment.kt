package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.ExercisesAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentExercisesBinding
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.repositories.TestingRepository

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    private lateinit var viewBinding: FragmentExercisesBinding
    private lateinit var exercisesAdapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentExercisesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        exercisesAdapter.setOnItemClickListener(exerciseClickedListener)

        viewBinding.fabAddExercise.setOnClickListener(addExerciseListener)

        exercisesAdapter.differ.submitList(TestingRepository().getExercises().toList())
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

    private fun setupRecyclerView() {
        exercisesAdapter = ExercisesAdapter()
        viewBinding.rvExercises.apply {
            adapter = exercisesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}