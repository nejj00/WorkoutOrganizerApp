package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.RoutineSetsAdapter
import com.nejj.workoutorganizerapp.databinding.ActivityRoutineBinding
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise
import com.nejj.workoutorganizerapp.models.relations.WorkoutRoutineWithRoutineSets
import com.nejj.workoutorganizerapp.ui.dialogs.AddExerciseDialogFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.*
import com.nejj.workoutorganizerapp.util.Constants.Companion.ROUTINE_ARGUMENT_KEY

class RoutineFragment : Fragment(R.layout.activity_routine) {

    private lateinit var viewBinding: ActivityRoutineBinding
    private lateinit var routineSetsAdapter: RoutineSetsAdapter
    private val workoutRoutineViewModel: WorkoutRoutineMainViewModel by activityViewModels()
    private val routineSetViewModel: RoutineSetMainViewModel by activityViewModels()
    private val loggedWorkoutRoutineViewModel: LoggedWorkoutRoutineViewModel by activityViewModels()
    private val loggedRoutineSetViewModel: LoggedRoutineSetViewModel by activityViewModels()
    private val loggedExerciseSetViewModel: LoggedExerciseSetViewModel by activityViewModels()

    private val args: RoutineFragmentArgs by navArgs()

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

        //val workoutExercises = workoutRoutine.workoutExercises
        //routineSetsAdapter.differ.submitList(workoutRoutine.workoutExercises?.toList())

        if(workoutRoutine.routineId != null) {
            routineSetViewModel.getRoutineSetsWithExercise(workoutRoutine.routineId).observe(viewLifecycleOwner) {
                val routineSetsWithExercise = it
                routineSetsAdapter.differ.submitList(routineSetsWithExercise.toList())
            }
        }

        viewBinding.fabAddRoutineExercise.setOnClickListener {
            val addExerciseDialogFragment = AddExerciseDialogFragment()
            val fragmentManager = childFragmentManager

            addExerciseDialogFragment.arguments = Bundle().apply {
                putSerializable(ROUTINE_ARGUMENT_KEY, workoutRoutine)
            }

            addExerciseDialogFragment.show(fragmentManager, "addExerciseDialogFragment")

        }
    }

    private fun setupRecyclerView() {
        routineSetsAdapter = RoutineSetsAdapter()
        viewBinding.rvRoutineExercises.apply {
            adapter = routineSetsAdapter
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
                    R.id.miStart -> {
                        startWorkout()
                        true
                    }
                    android.R.id.home -> {
                        saveRoutine()
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun startWorkout() {
        val workoutRoutine = args.routine

        var workoutRoutineWithRoutineSets = WorkoutRoutineWithRoutineSets()
        if(workoutRoutine.routineId != null) {
            workoutRoutineViewModel.getWorkoutRoutineWithRoutineSets(workoutRoutine.routineId).observe(viewLifecycleOwner) {
                workoutRoutineWithRoutineSets = it
            }
        }

        val routineSetsWithExercise = mutableListOf<RoutineSetsWithExercise>()
        if(workoutRoutine.routineId != null) {
            routineSetViewModel.getRoutineSetsWithExercise(workoutRoutine.routineId).observe(viewLifecycleOwner) {
                routineSetsWithExercise.addAll(it)
            }
        }

        val loggedWorkoutRoutine = LoggedWorkoutRoutine(workoutRoutine)
//
//        loggedWorkoutRoutineViewModel.insertEntity(loggedWorkoutRoutine)
//        var loggedWorkoutRoutineId: Long = 0
//        loggedWorkoutRoutineViewModel.insertedId.observe(viewLifecycleOwner) {
//            loggedWorkoutRoutineId = it
//        }
//
//        // initialize logged routine sets from the routine sets loaded from the routine id
//        // insert logged routine sets
//        for(routineSet in routineSetsWithExercise) {
//            val loggedRoutineSet = LoggedRoutineSet(routineSet, loggedWorkoutRoutineId)
//            loggedRoutineSetViewModel.insertEntity(loggedRoutineSet)
//
//            // initialize exercise sets from the routine sets
//            // insert exercise sets
//            for(i in 1..loggedRoutineSet.warmupSetsCount) {
//                val loggedExerciseSet = LoggedExerciseSet(loggedRoutineSet, 0, true)
//                loggedExerciseSetViewModel.insertEntity(loggedExerciseSet)
//            }
//
//            for(i in 1..loggedRoutineSet.setsCount) {
//                val loggedExerciseSet = LoggedExerciseSet(loggedRoutineSet, i, false)
//                loggedExerciseSetViewModel.insertEntity(loggedExerciseSet)
//            }
//        }
        loggedWorkoutRoutineViewModel.initializeLoggedWorkoutRoutine(loggedWorkoutRoutine, routineSetsWithExercise)

        loggedWorkoutRoutineViewModel.initializedLoggedWorkoutRoutineWithLoggedRoutineSets.observe(viewLifecycleOwner) {
            val loggedWorkoutRoutineWorkoutRoutineSets = it

            val bundle = Bundle().apply {
                putSerializable("loggedWorkoutRoutine", loggedWorkoutRoutineWorkoutRoutineSets)
            }
            findNavController().navigate(
                R.id.action_routineFragment_to_workoutFragment,
                bundle
            )
        }
    }
    private fun saveRoutine() {
        val workoutRoutine = args.routine
        val workoutRoutineName = viewBinding.tiRoutineName.editText?.text.toString()
        val workoutRoutineNotes = viewBinding.tiRoutineNotes.editText?.text.toString()

        if(workoutRoutineName.isNotEmpty()) {
            workoutRoutine.name = workoutRoutineName
            workoutRoutine.notes = workoutRoutineNotes
            workoutRoutineViewModel.insertEntity(workoutRoutine)
        }
    }
}