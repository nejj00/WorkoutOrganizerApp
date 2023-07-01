package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.RoutineAdapter
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.ui.viewmodels.WorkoutRoutineMainViewModel

class RoutinesFragment : ItemsListViewFragment<WorkoutRoutine>() {

    private val workoutRoutineViewModel: WorkoutRoutineMainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutRoutineViewModel.getEntities().observe(viewLifecycleOwner) { routines ->
            simpleItemPreviewAdapter.differ.submitList(routines)
        }
    }

    override val itemClickedListener = fun(routine: WorkoutRoutine) {
        val bundle = Bundle().apply {
            putSerializable("routine", routine)
        }
        findNavController().navigate(
            R.id.action_routinesFragment_to_routineFragment,
            bundle
        )
    }
    override val itemOptionsListener = fun(workoutRoutine: WorkoutRoutine, optionsView: View) {
        val popupMenu = PopupMenu(activity, optionsView, Gravity.BOTTOM)
        popupMenu.inflate(R.menu.categories_bottom_popup_menu)

        popupMenu.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.editCategory -> {
                    itemClickedListener(workoutRoutine)
                    return@setOnMenuItemClickListener true
                }
                R.id.deleteCategory -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this exercise?")
                        .setNegativeButton("Cancel") { dialog, which ->
                            return@setNegativeButton
                        }
                        .setPositiveButton("Delete") { dialog, which ->
                            workoutRoutineViewModel.deleteEntity(workoutRoutine)
                        }
                        .show()
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

        popupMenu.show()
    }

    override val addItemListener = fun(_: View) {
        val bundle = Bundle().apply {
            putSerializable("routine", WorkoutRoutine())
        }
        findNavController().navigate(
            R.id.action_routinesFragment_to_routineFragment,
            bundle
        )
    }

    override fun getAdapter(): SimpleItemPreviewAdapter<WorkoutRoutine> {
        return RoutineAdapter()
    }
}