package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.ExercisesAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentExercisesBinding
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.repositories.TestingRepository
import com.nejj.workoutorganizerapp.ui.viewmodels.ExercisesMainViewModel

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    private lateinit var viewBinding: FragmentExercisesBinding
    private lateinit var exercisesAdapter: ExercisesAdapter
    private val exercisesViewModel: ExercisesMainViewModel by activityViewModels()

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
        exercisesAdapter.setOnOptionsClickListener(onOptionsClickedListener)

        viewBinding.fabAddExercise.setOnClickListener(addExerciseListener)

        exercisesViewModel.getEntities().observe(viewLifecycleOwner, Observer {exercisesList ->
            exercisesAdapter.differ.submitList(exercisesList)
        })

        //exercisesAdapter.differ.submitList(TestingRepository().getExercises().toList())
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

    private val onOptionsClickedListener = fun(exercise: Exercise, optionsView: View) {
        val popupMenu = PopupMenu(activity, optionsView, Gravity.BOTTOM)
        popupMenu.inflate(R.menu.categories_bottom_popup_menu)

        popupMenu.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.editCategory -> {
                    exerciseClickedListener(exercise)
                    return@setOnMenuItemClickListener true
                }
                R.id.deleteCategory -> {
                    exercisesViewModel.deleteEntity(exercise)
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

        popupMenu.show()
    }

    private fun setupRecyclerView() {
        exercisesAdapter = ExercisesAdapter()
        viewBinding.rvExercises.apply {
            adapter = exercisesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}