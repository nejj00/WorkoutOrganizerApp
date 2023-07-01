package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.databinding.ItemsListViewFragmentBinding

abstract class ItemsListViewFragment<T> : Fragment(R.layout.items_list_view_fragment) {

    protected lateinit var viewBinding: ItemsListViewFragmentBinding
    lateinit var simpleItemPreviewAdapter: SimpleItemPreviewAdapter<T>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ItemsListViewFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        simpleItemPreviewAdapter.setOnItemClickListener(itemClickedListener)

        simpleItemPreviewAdapter.setOnOptionsClickListener(itemOptionsListener)

        viewBinding.fabAddItem.setOnClickListener(addItemListener)
    }

    protected abstract val itemClickedListener: (T) -> Unit

    protected abstract val addItemListener: (View) -> Unit

    protected open val itemOptionsListener: (T, View) -> Unit = {
        item, view ->
    }

    protected abstract fun getAdapter() : SimpleItemPreviewAdapter<T>

    private fun setupRecyclerView() {
        simpleItemPreviewAdapter = getAdapter()
        viewBinding.rvItems.apply {
            adapter = simpleItemPreviewAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}