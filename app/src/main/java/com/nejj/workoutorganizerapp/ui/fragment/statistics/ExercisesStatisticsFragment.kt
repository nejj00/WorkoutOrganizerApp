package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.adapters.StatisticsOptionsAdapter
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.ui.fragment.ItemsListViewFragment

class ExercisesStatisticsFragment : ItemsListViewFragment<StatisticsType>() {

    private val args: ExercisesStatisticsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exercise = args.exercise

        val overallStatistics = StatisticsType.values()
        simpleItemPreviewAdapter.differ.submitList(overallStatistics.toList())
    }

    override val itemClickedListener = fun(statistic: StatisticsType) {

    }
    override val addItemListener = fun(_: View) {

    }

    override fun getAdapter(): SimpleItemPreviewAdapter<StatisticsType> {
        return StatisticsOptionsAdapter(true)
    }
}