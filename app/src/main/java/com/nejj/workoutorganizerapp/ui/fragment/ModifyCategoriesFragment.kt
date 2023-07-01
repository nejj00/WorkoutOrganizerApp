package com.nejj.workoutorganizerapp.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import androidx.core.view.marginLeft
import androidx.core.view.setPadding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.sign_in.GoogleAuthUiClient

class ModifyCategoriesFragment : CategoriesFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesAdapter.setOnItemClickListener {
            addCategoryDialog(view, it).show()
        }

        categoriesAdapter.setOnOptionsClickListener { category, optionsView ->
            onOptionsClickedListener(view, category, optionsView)
        }

        viewBinding.fabAddCategory.setOnClickListener {
            addCategoryDialog(view, ExerciseCategory(null, "")).show()
        }
    }

    private fun onOptionsClickedListener(view: View, category: ExerciseCategory, optionsView: View) {
        val popupMenu = PopupMenu(activity, optionsView, Gravity.BOTTOM)
        popupMenu.inflate(R.menu.categories_bottom_popup_menu)

        popupMenu.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.editCategory -> {
                    addCategoryDialog(view, category).show()
                    return@setOnMenuItemClickListener true
                }
                R.id.deleteCategory -> {
                    categoriesViewModel.checkIfCategoryIsUsed(category).observe(viewLifecycleOwner) { result ->
                        if(result)
                            Snackbar.make(view, "Category is being used in exercises.", Snackbar.LENGTH_SHORT).show()
                        else {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to delete this category?")
                                .setNegativeButton("Cancel") { dialog, which ->
                                    return@setNegativeButton
                                }
                                .setPositiveButton("Delete") { dialog, which ->
                                    categoriesViewModel.deleteEntity(category)
                                    Snackbar.make(view, "Category deleted", Snackbar.LENGTH_SHORT).show()
                                }
                                .show()
                        }
                    }
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

        popupMenu.show()
    }

    private fun addCategoryDialog(view: View, exerciseCategory: ExerciseCategory) : androidx.appcompat.app.AlertDialog {
        val inputDialogLayout = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_input_text, null) as View
        val inputEditText = inputDialogLayout.findViewById<EditText>(R.id.etDialogInput)
        inputEditText.setText(exerciseCategory.name)
        inputEditText.hint = "Name"

        val materialDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Category")
            .setView(inputDialogLayout)
            .setPositiveButton("Add", DialogInterface.OnClickListener { dialogInterface, i ->
                if(inputEditText.text.toString().isNotEmpty()) {
                    exerciseCategory.name = inputEditText.text.toString()
                    exerciseCategory.isUserMade = true
                    exerciseCategory.userUID = Firebase.auth.currentUser?.uid

                    categoriesViewModel.insertEntity(exerciseCategory)
                    Snackbar.make(view, "Category saved successfully", Snackbar.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            .create()

        return materialDialog
    }
}