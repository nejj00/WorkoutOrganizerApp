package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.LoggedRoutineSetInWorkoutAdapter
import com.nejj.workoutorganizerapp.databinding.ActivityWorkoutBinding
import com.nejj.workoutorganizerapp.enums.AddExerciseDialogContext
import com.nejj.workoutorganizerapp.enums.FragmentContext
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.relations.LoggedRoutineSetWithLoggedExerciseSet
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets
import com.nejj.workoutorganizerapp.ui.dialogs.AddExerciseDialogFragment
import com.nejj.workoutorganizerapp.ui.dialogs.CountdownTimerBottomSheet
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedExerciseSetViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedRoutineSetViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedWorkoutRoutineViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class WorkoutFragment : Fragment(R.layout.activity_workout) {

    private lateinit var viewBinding: ActivityWorkoutBinding
    private lateinit var loggedRoutineSetInWorkoutAdapter: LoggedRoutineSetInWorkoutAdapter
    private val args: WorkoutFragmentArgs by navArgs()
    private val loggedWorkoutRoutineViewModel: LoggedWorkoutRoutineViewModel by activityViewModels()
    private val loggedRoutineSetViewModel: LoggedRoutineSetViewModel by activityViewModels()
    private val loggedExerciseSetViewModel: LoggedExerciseSetViewModel by activityViewModels()

    private val localizedTImeFormatter = DateTimeFormatter.ofPattern("HH:mm")

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

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveLoggedWorkoutRoutine()
            findNavController().navigateUp()
        }

        val loggedWorkoutRoutineWithLoggedSets = args.loggedWorkoutRoutine
        viewBinding.tfWorkoutName.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.name)
        viewBinding.tfWorkoutNotes.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.notes)
        viewBinding.tfBodyweight.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.bodyweight.toString())
        viewBinding.tfWotkoutDate.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.date.toString())
        viewBinding.tfWorkoutStartTime.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.startTime.format(localizedTImeFormatter))

        if(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.endTime != null)
            viewBinding.tfWorkoutEndTime.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.endTime!!.format(localizedTImeFormatter))

        viewBinding.tfWotkoutDate.editText?.setOnClickListener  {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                datePicker.addOnPositiveButtonClickListener { selectedDate ->
                    val localDate = Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()

                    viewBinding.tfWotkoutDate.editText?.setText(localDate.toString())
                }

                datePicker.show(childFragmentManager, "datePicker")
        }

        viewBinding.tfWorkoutStartTime.editText?.setOnClickListener(timeClickedListener)
        viewBinding.tfWorkoutEndTime.editText?.setOnClickListener(timeClickedListener)

        if(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.endTime != null)
            viewBinding.tfWorkoutEndTime.editText?.setText(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.endTime.toString().substring(0, 5))

        loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.loggedRoutineId?.let { loggedRoutineId ->
            loggedRoutineSetViewModel
                .getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId)
                .observe(viewLifecycleOwner) { loggedRoutineSetsWithLoggedExerciseSets ->
                    loggedRoutineSetInWorkoutAdapter.differ.submitList(loggedRoutineSetsWithLoggedExerciseSets.toList().sortedBy { it.loggedRoutineSet.setsOrder })
                }
        }

        loggedRoutineSetInWorkoutAdapter.setOnOptionsClickListener(loggedRoutineSetOptionsClickListener)
        loggedRoutineSetInWorkoutAdapter.setOnAddSetClickListener(addExerciseSetClickedListener)
        loggedRoutineSetInWorkoutAdapter.setOnExerciseSetOptionsClickListener(exerciseSetOptionsClickListener)

        viewBinding.fabAddWorkoutExercise.setOnClickListener{

            if(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.loggedRoutineId == null) {
                loggedWorkoutRoutineViewModel.insertEntityAndGetID(loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine)

                loggedWorkoutRoutineViewModel.loggedWorkoutRoutineID.observe(viewLifecycleOwner) { loggedWorkoutRoutineID ->
                    loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine.loggedRoutineId = loggedWorkoutRoutineID
                    openAddExerciseFragment(loggedWorkoutRoutineWithLoggedSets)
                }
            } else {
                openAddExerciseFragment(loggedWorkoutRoutineWithLoggedSets)
            }
        }
    }

    private fun openAddExerciseFragment(loggedWorkoutRoutineWithLoggedSets: LoggedWorkoutRoutineWithLoggedRoutineSets) {
        val addExerciseDialogFragment = AddExerciseDialogFragment()
        val fragmentManager = childFragmentManager

        addExerciseDialogFragment.arguments = Bundle().apply {
            putSerializable("addDialogContext", AddExerciseDialogContext.ADD_LOGGED_ROUTINE_SET)
            putSerializable(
                "loggedWorkoutRoutine",
                loggedWorkoutRoutineWithLoggedSets.loggedWorkoutRoutine
            )
        }

        addExerciseDialogFragment.show(fragmentManager, "addExerciseDialogFragment")
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
                        val modalBottomSheet = CountdownTimerBottomSheet()
                        modalBottomSheet.show(childFragmentManager, CountdownTimerBottomSheet.TAG)
                        true
                    }
                    R.id.miEditLoggedRoutine -> {
                        reorderRoutineSets()
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

    private fun reorderRoutineSets() {
        val bundle = Bundle().apply {
            putSerializable("routineId", args.loggedWorkoutRoutine.loggedWorkoutRoutine.loggedRoutineId)
            putSerializable("fragmentContext", FragmentContext.WORKOUT_CONTEXT)
        }
        findNavController().navigate(
            R.id.action_workoutFragment_to_reorderFragment,
            bundle
        )
    }

    private val timeClickedListener = fun(editText: View) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select Start Time")
                .build()


        timePicker.addOnPositiveButtonClickListener {
            val hour = if (timePicker.hour < 10) "0${timePicker.hour}" else timePicker.hour
            val minute = if (timePicker.minute < 10) "0${timePicker.minute}" else timePicker.minute

            val timeEditText = editText as EditText
            timeEditText.setText("$hour:$minute")
        }

        timePicker.show(childFragmentManager, "timePicker")
    }

    private fun saveLoggedWorkoutRoutine() {
        val loggedWorkoutRoutine = args.loggedWorkoutRoutine.loggedWorkoutRoutine
        val loggedRoutineName = viewBinding.tfWorkoutName.editText?.text.toString()
        val bodyweight = viewBinding.tfBodyweight.editText?.text.toString()
        val loggedRoutineNotes = viewBinding.tfWorkoutNotes.editText?.text.toString()
        val workoutDate = viewBinding.tfWotkoutDate.editText?.text.toString()
        val workoutStartTime = viewBinding.tfWorkoutStartTime.editText?.text.toString()
        val workoutEndTime = viewBinding.tfWorkoutEndTime.editText?.text.toString()

        if(loggedRoutineName.isNotEmpty())
            loggedWorkoutRoutine.name = loggedRoutineName

        if(bodyweight.isNotEmpty())
            loggedWorkoutRoutine.bodyweight = bodyweight.toDouble()

        if(loggedRoutineNotes.isNotEmpty())
            loggedWorkoutRoutine.notes = loggedRoutineNotes

        if(workoutDate.isNotEmpty())
            loggedWorkoutRoutine.date = LocalDate.parse(workoutDate, DateTimeFormatter.ISO_LOCAL_DATE)

        if(workoutStartTime.isNotEmpty())
            loggedWorkoutRoutine.startTime = LocalTime.parse(workoutStartTime, DateTimeFormatter.ISO_LOCAL_TIME)

        if(workoutEndTime.isNotEmpty())
            loggedWorkoutRoutine.endTime = LocalTime.parse(workoutEndTime, DateTimeFormatter.ISO_LOCAL_TIME)

        loggedWorkoutRoutine.userUID = Firebase.auth.currentUser?.uid

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