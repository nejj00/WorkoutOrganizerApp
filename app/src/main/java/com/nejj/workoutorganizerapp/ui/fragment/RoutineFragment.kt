package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.RoutineSetsAdapter
import com.nejj.workoutorganizerapp.databinding.ActivityRoutineBinding
import com.nejj.workoutorganizerapp.enums.AddExerciseDialogContext
import com.nejj.workoutorganizerapp.enums.FragmentContext
import com.nejj.workoutorganizerapp.models.*
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise
import com.nejj.workoutorganizerapp.models.relations.WorkoutRoutineWithRoutineSets
import com.nejj.workoutorganizerapp.ui.dialogs.AddExerciseDialogFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.*
import com.nejj.workoutorganizerapp.util.Constants.Companion.ROUTINE_ARGUMENT_KEY
import kotlinx.coroutines.launch

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

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveRoutine()
            findNavController().navigateUp()
        }

        val workoutRoutine = args.routine
        viewBinding.tiRoutineName.editText?.setText(workoutRoutine.name)
        viewBinding.tiRoutineNotes.editText?.setText(workoutRoutine.notes)

        //val workoutExercises = workoutRoutine.workoutExercises
        //routineSetsAdapter.differ.submitList(workoutRoutine.workoutExercises?.toList())

        routineSetsAdapter.setOnItemClickListener(routineSetClickedListener)

        if(workoutRoutine.routineId != null) {
            routineSetViewModel.getRoutineSetsWithExerciseLive(workoutRoutine.routineId!!).observe(viewLifecycleOwner) {
                val routineSetsWithExercise = it
                routineSetsAdapter.differ.submitList(routineSetsWithExercise.toList())
            }
        }

        viewBinding.fabAddRoutineExercise.setOnClickListener {
            if(workoutRoutine.routineId == null) {
                val workoutRoutineName = viewBinding.tiRoutineName.editText?.text.toString()

                if(workoutRoutineName.isNotEmpty())
                    workoutRoutine.name = workoutRoutineName
                else {
                    Snackbar.make(view, "Please enter a name for the routine", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                workoutRoutineViewModel.insertEntityAndGetID(workoutRoutine)
                workoutRoutineViewModel.workoutRoutineID.observe(viewLifecycleOwner) { routineId ->
                    workoutRoutine.routineId = routineId
                    openAddExerciseFragment(workoutRoutine)
                }
            } else {
                openAddExerciseFragment(workoutRoutine)
            }
        }
    }

    private fun openAddExerciseFragment(workoutRoutine: WorkoutRoutine) {
        val addExerciseDialogFragment = AddExerciseDialogFragment()
        val fragmentManager = childFragmentManager
        addExerciseDialogFragment.arguments = Bundle().apply {
            putSerializable("addDialogContext", AddExerciseDialogContext.ADD_ROUTINE_SET)
            putSerializable(ROUTINE_ARGUMENT_KEY, workoutRoutine)
        }

        addExerciseDialogFragment.show(fragmentManager, "addExerciseDialogFragment")
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
                    R.id.miReorder -> {
                        reorderRoutineSets()
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

    private fun reorderRoutineSets() {
        val bundle = Bundle().apply {
            putSerializable("routineId", args.routine.routineId)
            putSerializable("fragmentContext", FragmentContext.ROUTINE_CONTEXT)
        }
        findNavController().navigate(
            R.id.action_routineFragment_to_reorderFragment,
            bundle
        )
    }

    private val routineSetClickedListener = fun(routineSetWithExercise: RoutineSetsWithExercise) {
        val bundle = Bundle().apply {
            putSerializable("routineSet", routineSetWithExercise.routineSet)
        }
        findNavController().navigate(
            R.id.action_routineFragment_to_editRoutineSetFragment,
            bundle
        )
    }

    private fun startWorkout() {
        val workoutRoutine = args.routine

        if(workoutRoutine.routineId != null) {
            lifecycleScope.launch {
                val routineSetsWithExercise = routineSetViewModel.getRoutineSetsWithExercise(workoutRoutine.routineId!!)

                val loggedWorkoutRoutine = LoggedWorkoutRoutine(workoutRoutine)

                val loggedWorkoutRoutineWorkoutRoutineSets = loggedWorkoutRoutineViewModel.initializeLoggedWorkoutRoutine(loggedWorkoutRoutine, routineSetsWithExercise)

                val bundle = Bundle().apply {
                    putSerializable("loggedWorkoutRoutine", loggedWorkoutRoutineWorkoutRoutineSets)
                }
                findNavController().navigate(
                    R.id.action_routineFragment_to_workoutFragment,
                    bundle
                )
            }
        }
    }

    private fun saveRoutine() {
        val workoutRoutine = args.routine
        val workoutRoutineName = viewBinding.tiRoutineName.editText?.text.toString()
        val workoutRoutineNotes = viewBinding.tiRoutineNotes.editText?.text.toString()

        if(workoutRoutineName.isNotEmpty()) {
            workoutRoutine.name = workoutRoutineName
            workoutRoutine.notes = workoutRoutineNotes
            workoutRoutine.isUserMade = true
            workoutRoutine.userUID = Firebase.auth.currentUser?.uid
            workoutRoutineViewModel.insertEntity(workoutRoutine)
        }
    }
}