package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.models.Exercise
import java.util.*

class ModifyExercisesFragment : ExercisesFragment() {

    val exerciseList = mutableListOf<Exercise>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAppBarMenuItems()

        exercisesAdapter.setOnItemClickListener(exerciseClickedListener)
        exercisesAdapter.setOnOptionsClickListener(onOptionsClickedListener)

        exercisesViewModel.getEntities().observe(viewLifecycleOwner) { exercisesList ->
            exerciseList.addAll(exercisesList)
            exercisesAdapter.differ.submitList(exercisesList)
        }

        viewBinding.fabAddExercise.setOnClickListener(addExerciseListener)
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

    private fun setUpAppBarMenuItems() {
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_exercise_toolbar, menu)
                val searchItem = menu.findItem(R.id.searchExercise)
                val searchView = searchItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        exerciseList.filter { exercise ->
                            exercise.name.lowercase(Locale.ROOT).contains(newText.toString().lowercase(), true)
                        }.let {
                            exercisesAdapter.differ.submitList(it)
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.addExercise -> {
                        return true
                    }
                }
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}