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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.CategoriesAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentCategoriesBinding
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.ui.MainActivity
import com.nejj.workoutorganizerapp.ui.viewmodels.CategoriesMainViewModel

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private lateinit var viewBinding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private val categoriesViewModel: CategoriesMainViewModel by activityViewModels()

    val TAG = "CategoriesFragment"

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
                        categoriesViewModel.deleteEntity(category)
                        return@setOnMenuItemClickListener true
                    }
                }
                return@setOnMenuItemClickListener false
            }

            popupMenu.show()
        }

        val allCategories : MutableList<ExerciseCategory> = mutableListOf()
        categoriesViewModel.getEntities().observe(viewLifecycleOwner, Observer { categories ->
            //allCategories.addAll(categories)
            categoriesAdapter.differ.submitList(categories)
        })

//        categoriesViewModel.categories.observe(viewLifecycleOwner, Observer { response ->
//            when(response) {
//                is Resource.Success -> {
//                    response.data?.let { categoriesResponse ->
//                        //allCategories.addAll(categoriesResponse.categories)
//                        categoriesAdapter.differ.submitList(categoriesResponse.categories)
//                    }
//                }
//                is Resource.Error -> {
//                    response.message?.let { message ->
//                        Log.e(TAG, "An error occured: $message")
//                    }
//                }
//                is Resource.Loading -> {
//
//                }
//            }
//        })

        //categoriesAdapter.differ.submitList(allCategories)
        //categoriesAdapter.differ.submitList(TestingRepository().getCategories().toList())

        viewBinding.fabAddCategory.setOnClickListener {
            addCategoryDialog(view, ExerciseCategory(null, "")).show()
        }
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
                    categoriesViewModel.insertEntity(exerciseCategory)
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