package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.FragmentEditExerciseBinding
import com.nejj.workoutorganizerapp.enums.ExerciseType
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.ui.MainActivity
import com.nejj.workoutorganizerapp.ui.viewmodels.CategoriesMainViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.ExercisesMainViewModel

class EditExerciseFragment : Fragment(R.layout.fragment_edit_exercise) {

    private lateinit var viewBinding: FragmentEditExerciseBinding
    private val args: EditExerciseFragmentArgs by navArgs()
    private lateinit var categoriesViewModel: CategoriesMainViewModel
    private val exercisesViewModel: ExercisesMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEditExerciseBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAppBarMenuItems()

        // setup categories dropdown
        categoriesViewModel = (activity as MainActivity).categoriesViewModel

        val exerciseCategoryList: MutableList<ExerciseCategory> = mutableListOf()
        categoriesViewModel.getEntities().observe(viewLifecycleOwner) { exerciseCategory ->
            exerciseCategoryList.addAll(exerciseCategory)
        }
        val adapter = ArrayAdapter<ExerciseCategory>(
            activity as MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, exerciseCategoryList)

        val categoryAutoCompleteTextView = viewBinding.tiCategory.editText as? AutoCompleteTextView
        categoryAutoCompleteTextView?.setAdapter(adapter)

        categoryAutoCompleteTextView?.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as ExerciseCategory
            Toast.makeText(
                activity,
                "You chose $selectedItem",
                Toast.LENGTH_LONG).show()
        }

        // setup exercise type dropdown
        val exerciseTypeList = ExerciseType.values()
        val exerciseTypeAdapter = ArrayAdapter(
            activity as MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, exerciseTypeList)

        val exerciseTypeAutoCompleteTextView = viewBinding.tiExerciseType.editText as? AutoCompleteTextView
        exerciseTypeAutoCompleteTextView?.setAdapter(exerciseTypeAdapter)

        val exercise = args.exercise
        viewBinding.tiExerciseName.editText?.setText(exercise.name)
        viewBinding.actvCategory.setText(exercise.category, false)
        viewBinding.actvExerciseType.setText(exercise.type, false)
        viewBinding.cbIsSingleSide.isChecked = exercise.isSingleSide;
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
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.miSave -> {
                        saveExercise()
                        findNavController().navigateUp()
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveExercise(): Boolean {
        val name = viewBinding.tiExerciseName.editText?.text.toString()
        val category = viewBinding.tiCategory.editText?.text.toString()
        val exerciseType = viewBinding.tiExerciseType.editText?.text.toString()
        val isSingleSide = viewBinding.cbIsSingleSide.isChecked

        if (name.isEmpty() || category.isEmpty() || exerciseType.isEmpty()) {
            Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_LONG).show()

            return false
        } else {
            exercisesViewModel.insertEntity(
                Exercise(args.exercise.exerciseId, name, category, exerciseType, isSingleSide, true)
            )
            Toast.makeText(activity, "Exercise saved", Toast.LENGTH_LONG).show()
        }

        return true
    }
}