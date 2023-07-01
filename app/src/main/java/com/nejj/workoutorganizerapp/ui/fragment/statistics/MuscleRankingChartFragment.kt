package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAScrollablePlotArea
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.FragmentStatisticChartBinding

class MuscleRankingChartFragment : Fragment(R.layout.fragment_statistic_chart) {

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

        val aaChartView = viewBinding.aaChartView as AAChartView
        val scrollablePlotArea = AAScrollablePlotArea()
        scrollablePlotArea.minHeight = aaChartView.height
        scrollablePlotArea.minWidth = aaChartView.width
        scrollablePlotArea.opacity = 80f

        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("title")
            .subtitle("subtitle")
            .dataLabelsEnabled(true)
            .backgroundColor("#FF000000")
            .axesTextColor("#FFFFFFFF")
            .scrollablePlotArea(
                scrollablePlotArea
            )
            .polar(true)
            .dataLabelsEnabled(false)
            .touchEventEnabled(true)
            .zoomType(AAChartZoomType.X)
            .categories(categoriesArray)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name(statisticsDataSet.overallStatisticsType.toString())
                        .data(dataSetArray),
                )
            )

        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }
}