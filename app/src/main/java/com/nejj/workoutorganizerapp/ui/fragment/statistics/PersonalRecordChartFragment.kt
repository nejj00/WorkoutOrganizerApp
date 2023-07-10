package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartZoomType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.PersonalRecordHistoryAdapter
import com.nejj.workoutorganizerapp.adapters.RecordHistoryItem
import com.nejj.workoutorganizerapp.databinding.FragmentPersonalRecordChartBinding
import com.nejj.workoutorganizerapp.enums.ExerciseStatisticsType
import com.nejj.workoutorganizerapp.enums.PersonalRecordStatisticsType
import com.nejj.workoutorganizerapp.ui.viewmodels.StatisticsViewModel
import kotlinx.coroutines.launch

class PersonalRecordChartFragment : Fragment(R.layout.fragment_personal_record_chart) {

    private lateinit var viewBinding: FragmentPersonalRecordChartBinding
    private lateinit var personalRecordsHistoryAdapter: PersonalRecordHistoryAdapter
    private val statisticsViewModel: StatisticsViewModel by activityViewModels()

    private val args: PersonalRecordChartFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPersonalRecordChartBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        val aaChartView = viewBinding.aaChartPersonalRecord

        lifecycleScope.launch {
            val dateDataSetMap = statisticsViewModel.getLoggedExerciseSetsWithDateMapByExerciseDistinct(args.personalRecord.personalRecordStatisticsType, args.personalRecord.exerciseId!!)

            val(categories, dataSet) = dateDataSetMap.toList().unzip()

            val recordsHistory = mutableListOf<RecordHistoryItem>()
            dateDataSetMap.forEach {
                recordsHistory.add(RecordHistoryItem(it.key, it.value.toString().toDouble()))
            }

            personalRecordsHistoryAdapter.differ.submitList(recordsHistory.sortedByDescending { it.date })

            val aaChartModel : AAChartModel = AAChartModel()
                .chartType(AAChartType.Areaspline)
                .title(args.personalRecord.personalRecordStatisticsType.toString())
                .dataLabelsEnabled(true)
                .backgroundColor("#FF000000")
                .axesTextColor("#FFFFFFFF")
                .dataLabelsEnabled(false)
                .touchEventEnabled(true)
                .zoomType(AAChartZoomType.X)
                .categories(categories.toTypedArray())
                .series(arrayOf(
                    AASeriesElement()
                        .name(args.personalRecord.personalRecordStatisticsType.toString())
                        .data(dataSet.toTypedArray()),
                )
                )

            aaChartView.aa_drawChartWithChartModel(aaChartModel)
        }
    }

    private fun setUpRecyclerView() {
        personalRecordsHistoryAdapter = PersonalRecordHistoryAdapter()
        viewBinding.rvHistory.apply {
            adapter = personalRecordsHistoryAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}