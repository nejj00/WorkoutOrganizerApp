package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.LoggedRoutineSetInWorkoutAdapter
import com.nejj.workoutorganizerapp.databinding.ActivityWorkoutBinding
import com.nejj.workoutorganizerapp.enums.AddExerciseDialogContext
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.relations.LoggedRoutineSetWithLoggedExerciseSet
import com.nejj.workoutorganizerapp.ui.dialogs.AddExerciseDialogFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedExerciseSetViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedRoutineSetViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedWorkoutRoutineViewModel
import com.nejj.workoutorganizerapp.util.Constants

class WorkoutFragment : Fragment(R.layout.activity_workout) {

    private lateinit var viewBinding: ActivityWorkoutBinding
    private lateinit var loggedRoutineSetInWorkoutAdapter: LoggedRoutineSetInWorkoutAdapter
    private val args: WorkoutFragmentArgs by navArgs()
    private val loggedWorkoutRoutineViewModel: LoggedWorkoutRoutineViewModel by activityViewModels()
    private val loggedRoutineSetViewModel: LoggedRoutineSetViewModel by activityViewModels()
    private val loggedExerciseSetViewModel: LoggedExerciseSetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = ActivityWorkoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setUpAppBarMenuItems()

        val loggedWorkoutRoutineWithLoggedSets = args.loggedWorkoutRoutine
        viewBinding.tfWorkoutName.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.name)
        viewBinding.tfWorkoutNotes.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.notes)

        loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.loggedRoutineId?.let { loggedRoutineId ->
            loggedRoutineSetViewModel
                .getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId)
                .observe(viewLifecycleOwner) { loggedRoutineSetsWithLoggedExerciseSets ->
                    loggedRoutineSetInWorkoutAdapter.differ.submitList(loggedRoutineSetsWithLoggedExerciseSets.toList())
                }
        }

        loggedRoutineSetInWorkoutAdapter.setOnOptionsClickListener(loggedRoutineSetOptionsClickListener)
        loggedRoutineSetInWorkoutAdapter.setOnAddSetClickListener(addExerciseSetClickedListener)
        loggedRoutineSetInWorkoutAdapter.setOnExerciseSetOptionsClickListener(exerciseSetOptionsClickListener)

        viewBinding.fabAddWorkoutExercise.setOnClickListener{
            val addExerciseDialogFragment = AddExerciseDialogFragment()
            val fragmentManager = childFragmentManager

            addExerciseDialogFragment.arguments = Bundle().apply {
                putSerializable("addDialogContext", AddExerciseDialogContext.ADD_LOGGED_ROUTINE_SET)
                putSerializable("loggedWorkoutRoutine", loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine)
            }

            addExerciseDialogFragment.show(fragmentManager, "addExerciseDialogFragment")
        }
    }

    private fun setupRecyclerView() {
        loggedRoutineSetInWorkoutAdapter = LoggedRoutineSetInWorkoutAdapter()
        viewBinding.rvWorkoutExercises.apply {
            adapter = loggedRoutineSetInWorkoutAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setUpAppBarMenuItems() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_logged_routine_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.miExerciseTimer -> {
                        true
                    }
                    R.id.miEditLoggedRoutine -> {
                        true
                    }
                    android.R.id.home -> {
                        saveLoggedWorkoutRoutine()
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveLoggedWorkoutRoutine() {
        val loggedWorkoutRoutine = args.loggedWorkoutRoutine.loggedWorkoutRoutine
        val loggedRoutineName = viewBinding.tfWorkoutName.editText?.text.toString()
        val bodyweight = viewBinding.tfBodyweight.editText?.text.toString()
        val loggedRoutineNotes = viewBinding.tfWorkoutNotes.editText?.text.toString()

        if(loggedRoutineName.isNotEmpty())
            loggedWorkoutRoutine.name = loggedRoutineName

        if(bodyweight.isNotEmpty())
            loggedWorkoutRoutine.bodyweight = bodyweight.toDouble()

        if(loggedRoutineNotes.isNotEmpty())
            loggedWorkoutRoutine.notes = loggedRoutineNotes

        loggedWorkoutRoutineViewModel.insertEntity(loggedWorkoutRoutine)

        val updatedExerciseSets = loggedRoutineSetInWorkoutAdapter.getExerciseSets()
        for(exerciseSet in updatedExerciseSets) {
            loggedExerciseSetViewModel.insertEntity(exerciseSet)
        }
    }

    private val loggedRoutineSetOptionsClickListener = fun(loggedRoutineSetsWithLoggedExerciseSet: LoggedRoutineSetWithLoggedExerciseSet, optionsView: View) {
        val popupMenu = PopupMenu(activity, optionsView, Gravity.BOTTOM)
        popupMenu.inflate(R.menu.popup_logged_routine_set)

        popupMenu.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.miReorderRoutineSets -> {

                    return@setOnMenuItemClickListener true
                }
                R.id.miDeleteRoutineSet -> {
                    loggedRoutineSetViewModel.deleteEntity(loggedRoutineSetsWithLoggedExerciseSet)
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

        popupMenu.show()
    }

    private val addExerciseSetClickedListener = fun(loggedRoutineSetsWithLoggedExerciseSet: LoggedRoutineSetWithLoggedExerciseSet) {
        loggedExerciseSetViewModel.insertNewExerciseSetToLoggedRoutineSet(loggedRoutineSetsWithLoggedExerciseSet.loggedRoutineSet)
    }

    private val exerciseSetOptionsClickListener = fun(loggedExerciseSet: LoggedExerciseSet, optionsView: View) {
        val popupMenu = PopupMenu(activity, optionsView, Gravity.BOTTOM)
        popupMenu.inflate(R.menu.popup_logged_exercise_set)

        popupMenu.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.miNormalExerciseSet -> {

                    return@setOnMenuItemClickListener true
                }
                R.id.miWarmupExerciseSet -> {

                    return@setOnMenuItemClickListener true
                }
                R.id.miDeleteExerciseSet -> {
                    loggedExerciseSetViewModel.deleteEntity(loggedExerciseSet)
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

        popupMenu.show()
    }
}