package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Adapter
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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
        setUpAppBarMenuItems()

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

    private fun setUpAppBarMenuItems() {
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.toolbar_menu_routine, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.miSaveRoutine -> {
                        // clearCompletedTasks()
                        true
                    }
                    R.id.miStart -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}