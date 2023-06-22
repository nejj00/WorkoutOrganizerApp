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
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.models.StatisticsDataSet
import com.nejj.workoutorganizerapp.ui.fragment.SimpleItemsDoubleListViewFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.CategoriesMainViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedWorkoutRoutineViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import java.time.LocalDate


class StatisticsFragment : SimpleItemsDoubleListViewFragment<StatisticsType, ExerciseCategory>() {

    private val categoriesViewModel: CategoriesMainViewModel by activityViewModels()
    private val loggedWorkoutRoutineViewModel: LoggedWorkoutRoutineViewModel by activityViewModels()

    companion object {
        val TAG = "StatisticsFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.getEntities().observe(viewLifecycleOwner) { categories ->
            //allCategories.addAll(categories)
            simpleItemPreviewBottomRVAdapter.differ.submitList(categories)
        }

        val overallStatistics = StatisticsType.values()

        simpleItemPreviewTopRVAdapter.differ.submitList(overallStatistics.toList())

        val timeZoneUTC = TimeZone.Companion
        Log.e(TAG, "The time is $timeZoneUTC")

//        val aaChartView = viewBinding.aaChartView
//
//        val aaChartModel : AAChartModel = AAChartModel()
//            .chartType(AAChartType.Line)
//            .title("title")
//            .subtitle("subtitle")
//            .backgroundColor("#4b2b7f")
//            .dataLabelsEnabled(true)
//            .series(arrayOf(
//                AASeriesElement()
//                    .name("Tokyo")
//                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
//                AASeriesElement()
//                    .name("NewYork")
//                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
//                AASeriesElement()
//                    .name("London")
//                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
//                AASeriesElement()
//                    .name("Berlin")
//                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
//            )
//            )
//
//        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    override val topTVItemClickedListener = fun(statistic: StatisticsType) {
        lifecycleScope.launch {
            val dateVolumeMap = loggedWorkoutRoutineViewModel.getVolumeStatisticsDataSetMap() as HashMap<String, Float>

            val(categories, dataSet) = dateVolumeMap.toList().unzip()

            val statisticsDataSet = StatisticsDataSet(categories, dataSet)

            val bundle = Bundle().apply {
                putSerializable("statisticsDataSet", statisticsDataSet)
            }

            findNavController().navigate(
                R.id.action_statisticsFragment_to_statisticsChartFragment,
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


