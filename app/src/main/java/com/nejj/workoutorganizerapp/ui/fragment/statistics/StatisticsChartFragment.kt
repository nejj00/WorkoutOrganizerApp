package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAScrollablePlotArea
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.FragmentStatisticChartBinding
import com.nejj.workoutorganizerapp.enums.OverallStatisticsType

class StatisticsChartFragment : Fragment(R.layout.fragment_statistic_chart) {

    private lateinit var viewBinding: FragmentStatisticChartBinding

    private val args: StatisticsChartFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentStatisticChartBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statisticsDataSet = args.statisticsDataSet

        val categoriesArray = statisticsDataSet.categories.toTypedArray()
        val dataSetArray = statisticsDataSet.dataSet.toTypedArray()

        val aaChartView = viewBinding.aaChartView

        val chartType = when (statisticsDataSet.overallStatisticsType.toString()) {
            OverallStatisticsType.TOTAL_SETS.toString() -> AAChartType.Column
            OverallStatisticsType.REPS_PER_SET.toString() -> AAChartType.Column
            else -> AAChartType.Spline
        }

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(chartType)
            .title(statisticsDataSet.overallStatisticsType.toString())
            .titleStyle(AAStyle().color("#FFFFFFFF"))
            .dataLabelsEnabled(true)
            .backgroundColor("#FF000000")
            .axesTextColor("#FFFFFFFF")
            .dataLabelsEnabled(false)
            .touchEventEnabled(true)
            .zoomType(AAChartZoomType.X)
            .categories(categoriesArray)
            .series(arrayOf(
                AASeriesElement()
                    .name(statisticsDataSet.overallStatisticsType.toString())
                    .data(dataSetArray),
            )
            )

        if(OverallStatisticsType.VOLUME.toString() == statisticsDataSet.overallStatisticsType.toString()) {
            aaChartModel.subtitle = "Calculated as weight * reps * sets"
            aaChartModel.subtitleStyle = (AAStyle().color("#FFFFFFFF"))
        }

        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }
}