package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.CategoriesAdapter
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.adapters.StatisticsOptionsAdapter
import com.nejj.workoutorganizerapp.enums.OverallStatisticsType
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.models.StatisticsDataSet
import com.nejj.workoutorganizerapp.ui.fragment.SimpleItemsDoubleListViewFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.CategoriesMainViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedWorkoutRoutineViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.StatisticsViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone


class StatisticsFragment : SimpleItemsDoubleListViewFragment<StatisticsType, ExerciseCategory>() {

    private val categoriesViewModel: CategoriesMainViewModel by activityViewModels()
    private val statisticsViewModel: StatisticsViewModel by activityViewModels()

    companion object {
        val TAG = "StatisticsFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.getEntities().observe(viewLifecycleOwner) { categories ->
            simpleItemPreviewBottomRVAdapter.differ.submitList(categories)
        }

        val overallStatistics = OverallStatisticsType.values()

        simpleItemPreviewTopRVAdapter.differ.submitList(overallStatistics.toList())
    }

    override val topTVItemClickedListener = fun(statisticType: StatisticsType) {
        lifecycleScope.launch {
            val dateVolumeMap = statisticsViewModel.getStatisticsDataSetMap(statisticType)

            val(categories, dataSet) = dateVolumeMap.toList().unzip()

            val statisticsDataSet = StatisticsDataSet(statisticType, categories, dataSet)

            val bundle = Bundle().apply {
                putSerializable("statisticsDataSet", statisticsDataSet)
            }

            val action = if(statisticType == OverallStatisticsType.MUSCLE_RANKING_CHART) {
                R.id.action_statisticsFragment_to_muscleRankingChartFragment
            } else {
                R.id.action_statisticsFragment_to_statisticsChartFragment
            }

            findNavController().navigate(
                action,
                bundle
            )
        }
    }
    override val bottomTVItemClickedListener = fun(exerciseCategory: ExerciseCategory) {
        val bundle = Bundle().apply {
            putSerializable("exerciseCategory", exerciseCategory)
        }
        findNavController().navigate(
            R.id.action_statisticsFragment_to_categoriesStatisticsFragment,
            bundle
        )
    }

    override fun getTopRVAdapter(): SimpleItemPreviewAdapter<StatisticsType> {
        return StatisticsOptionsAdapter(true)
    }

    override fun getBottomRVAdapter(): SimpleItemPreviewAdapter<ExerciseCategory> {
        return CategoriesAdapter(true)
    }

}


