package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.ExercisesAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentExercisesBinding
import com.nejj.workoutorganizerapp.ui.viewmodels.ExercisesMainViewModel

open class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    protected lateinit var viewBinding: FragmentExercisesBinding
    protected lateinit var exercisesAdapter: ExercisesAdapter
    protected val exercisesViewModel: ExercisesMainViewModel by activityViewModels()

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

        //exercisesAdapter.differ.submitList(TestingRepository().getExercises().toList())
    }

    protected open fun hideOptionsIcon() : Boolean {
        return false
    }

    private fun setupRecyclerView() {
        exercisesAdapter = ExercisesAdapter(hideOptionsIcon())
        viewBinding.rvExercises.apply {
            adapter = exercisesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}