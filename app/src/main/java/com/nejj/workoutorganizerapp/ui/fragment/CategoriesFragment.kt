package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.CategoriesAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentCategoriesBinding
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.ui.viewmodels.CategoriesMainViewModel

open class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    protected lateinit var viewBinding: FragmentCategoriesBinding
    protected lateinit var categoriesAdapter: CategoriesAdapter
    protected val categoriesViewModel: CategoriesMainViewModel by activityViewModels()

    protected val TAG = "CategoriesFragment"

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

        val allCategories : MutableList<ExerciseCategory> = mutableListOf()
        categoriesViewModel.getEntities().observe(viewLifecycleOwner) { categories ->
            //allCategories.addAll(categories)
            categoriesAdapter.differ.submitList(categories)
        }

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
    }

    protected open fun hideOptionsIcon() : Boolean {
        return false
    }
    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesAdapter(hideOptionsIcon())
        viewBinding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}