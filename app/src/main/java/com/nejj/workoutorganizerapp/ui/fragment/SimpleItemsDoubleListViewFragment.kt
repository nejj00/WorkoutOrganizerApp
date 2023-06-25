package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentStatisticsBinding

abstract class SimpleItemsDoubleListViewFragment<T, Y> : Fragment(R.layout.fragment_statistics) {

    private lateinit var viewBinding: FragmentStatisticsBinding
    open lateinit var simpleItemPreviewTopRVAdapter: SimpleItemPreviewAdapter<T>
    open lateinit var simpleItemPreviewBottomRVAdapter: SimpleItemPreviewAdapter<Y>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopRecyclerView()
        setupBottomRecyclerView()

        simpleItemPreviewTopRVAdapter.setOnItemClickListener(topTVItemClickedListener)

        simpleItemPreviewBottomRVAdapter.setOnItemClickListener(bottomTVItemClickedListener)
    }

    protected abstract val topTVItemClickedListener: (T) -> Unit

    protected abstract val bottomTVItemClickedListener: (Y) -> Unit

    protected abstract fun getTopRVAdapter() : SimpleItemPreviewAdapter<T>

    protected abstract fun getBottomRVAdapter() : SimpleItemPreviewAdapter<Y>

    protected open fun setupTopRecyclerView() {
        simpleItemPreviewTopRVAdapter = getTopRVAdapter()
        viewBinding.rvTopItems.apply {
            adapter = simpleItemPreviewTopRVAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    protected open fun setupBottomRecyclerView() {
        simpleItemPreviewBottomRVAdapter = getBottomRVAdapter()
        viewBinding.rvBottomItems.apply {
            adapter = simpleItemPreviewBottomRVAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}