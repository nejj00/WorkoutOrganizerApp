package com.nejj.workoutorganizerapp.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.CategoriesAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentCategoriesBinding
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.TestingRepository
import com.nejj.workoutorganizerapp.ui.MainActivity
import com.nejj.workoutorganizerapp.ui.viewmodels.CategoriesMainViewModel

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private lateinit var viewBinding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var categoriesViewModel: CategoriesMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        categoriesViewModel = (activity as MainActivity).viewModel

        categoriesAdapter.setOnItemClickListener {
            addCategoryDialog(view, it).show()
        }

        categoriesAdapter.setOnOptionsClickListener { category, optionsView ->
            val popupMenu = PopupMenu(activity, optionsView, Gravity.BOTTOM)
            popupMenu.inflate(R.menu.categories_bottom_popup_menu)

            popupMenu.setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.editCategory -> {
                        addCategoryDialog(view, category).show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.deleteCategory -> {
                        categoriesViewModel.deleteCategory(category)
                        return@setOnMenuItemClickListener true
                    }
                }
                return@setOnMenuItemClickListener false
            }

            popupMenu.show()
        }

        categoriesViewModel.getCategories().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.differ.submitList(categories)
        })

        //categoriesAdapter.differ.submitList(TestingRepository().getCategories().toList())

        viewBinding.fabAddCategory.setOnClickListener {
            addCategoryDialog(view, ExerciseCategory(null, "")).show()
        }
    }

    private fun addCategoryDialog(view: View, exerciseCategory: ExerciseCategory) : AlertDialog {
        val addCategoryInput = EditText(activity)
        addCategoryInput.setHint("Enter category name")
        addCategoryInput.inputType = InputType.TYPE_CLASS_TEXT
        addCategoryInput.setText(exerciseCategory.categoryName)

        val addCategoryDialog = AlertDialog.Builder(activity)
            .setTitle("Category")
            .setView(addCategoryInput)
            .setPositiveButton("Add", DialogInterface.OnClickListener { dialog, which ->
                if(addCategoryInput.text.toString().isNotEmpty()) {
                    exerciseCategory.categoryName = addCategoryInput.text.toString()
                    categoriesViewModel.insertCategory(exerciseCategory)
                    Snackbar.make(view, "Category saved successfully", Snackbar.LENGTH_SHORT).show()
                }
            }).create()

        return addCategoryDialog
    }

    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        viewBinding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}