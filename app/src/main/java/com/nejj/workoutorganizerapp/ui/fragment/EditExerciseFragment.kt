package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
    private val categoriesViewModel: CategoriesMainViewModel by activityViewModels()
    private val exercisesViewModel: ExercisesMainViewModel by activityViewModels()

    private var selectedCategoryId  = 0L
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
        val exercise = args.exercise

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            checkBeforeGoingBack()
        }

        // setup categories dropdown
        val exerciseCategoryList: MutableList<ExerciseCategory> = mutableListOf()
        categoriesViewModel.getEntities().observe(viewLifecycleOwner) { exerciseCategory ->
            exerciseCategoryList.addAll(exerciseCategory)
            viewBinding.actvCategory.setText(
                exerciseCategoryList.find { it.categoryId == exercise.categoryId }?.name ?: "", false)
            selectedCategoryId = exercise.categoryId ?: 0
        }
        val adapter = ArrayAdapter<ExerciseCategory>(
            activity as MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, exerciseCategoryList)

        val categoryAutoCompleteTextView = viewBinding.tiCategory.editText as? AutoCompleteTextView
        categoryAutoCompleteTextView?.setAdapter(adapter)

        categoryAutoCompleteTextView?.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as ExerciseCategory
            selectedCategoryId = selectedItem.categoryId!!
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



        requireActivity().title = exercise.name

        viewBinding.tiExerciseName.editText?.setText(exercise.name)

        viewBinding.actvExerciseType.setText(exercise.type, false)
        viewBinding.cbIsSingleSide.isChecked = exercise.isSingleSide
        viewBinding.cbIsSingleSide.visibility = View.GONE
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
                        if(saveExercise()) {
                            findNavController().navigateUp()
                        }
                        false
                    }
                    android.R.id.home -> {
                        checkBeforeGoingBack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
    private fun checkBeforeGoingBack() {
        val name = viewBinding.tiExerciseName.editText?.text.toString()
        val categoryId = selectedCategoryId
        val exerciseType = viewBinding.tiExerciseType.editText?.text.toString()

        if(name == args.exercise.name && categoryId == args.exercise.categoryId && exerciseType == args.exercise.type) {
            findNavController().navigateUp()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Discard changes?")
                .setMessage("Are you sure that you want to discard changes?")
                .setNegativeButton("No") { dialog, which ->
                    return@setNegativeButton
                }
                .setPositiveButton("Discard") { dialog, which ->
                    findNavController().navigateUp()
                }
                .show()
        }
    }

    private fun saveExercise(): Boolean {
        val name = viewBinding.tiExerciseName.editText?.text.toString()
        val categoryId = selectedCategoryId
        val exerciseType = viewBinding.tiExerciseType.editText?.text.toString()
        val isSingleSide = viewBinding.cbIsSingleSide.isChecked

        if (name.isEmpty() || categoryId == 0L || exerciseType.isEmpty()) {
            Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_LONG).show()

            return false
        } else {
            exercisesViewModel.insertEntity(
                Exercise(args.exercise.exerciseId, categoryId, name, exerciseType, isSingleSide, true, Firebase.auth.currentUser?.uid)
            )
            Toast.makeText(activity, "Exercise saved", Toast.LENGTH_LONG).show()
        }

        return true
    }
}