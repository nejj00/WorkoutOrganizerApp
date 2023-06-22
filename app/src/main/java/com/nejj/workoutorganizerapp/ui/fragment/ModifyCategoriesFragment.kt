package com.nejj.workoutorganizerapp.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import com.google.android.material.snackbar.Snackbar
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
                    categoriesViewModel.deleteEntity(category).observe(viewLifecycleOwner) {
                        if(!it)
                            Snackbar.make(view, "Category is being used in exercises.", Snackbar.LENGTH_SHORT).show()

                    }
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

        popupMenu.show()
    }

    private fun addCategoryDialog(view: View, exerciseCategory: ExerciseCategory) : AlertDialog {
        val addCategoryInput = EditText(activity)
        addCategoryInput.setHint("Enter category name")
        addCategoryInput.inputType = InputType.TYPE_CLASS_TEXT
        addCategoryInput.setText(exerciseCategory.name)

        val addCategoryDialog = AlertDialog.Builder(activity)
            .setTitle("Category")
            .setView(addCategoryInput)
            .setPositiveButton("Add", DialogInterface.OnClickListener { dialog, which ->
                if(addCategoryInput.text.toString().isNotEmpty()) {
                    exerciseCategory.name = addCategoryInput.text.toString()
                    exerciseCategory.isUserMade = true
                    exerciseCategory.userUID = Firebase.auth.currentUser?.uid

                    categoriesViewModel.insertEntity(exerciseCategory)
                    Snackbar.make(view, "Category saved successfully", Snackbar.LENGTH_SHORT).show()
                }
            }).create()

        return addCategoryDialog
    }
}