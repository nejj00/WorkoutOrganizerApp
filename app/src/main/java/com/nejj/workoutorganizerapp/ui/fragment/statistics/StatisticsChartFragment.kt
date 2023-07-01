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

        val aaChartView = viewBinding.aaChartView as AAChartView
        val scrollablePlotArea = AAScrollablePlotArea()
        scrollablePlotArea.minHeight = aaChartView.height
        scrollablePlotArea.minWidth = aaChartView.width
        scrollablePlotArea.opacity = 80f

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .title("title")
            .subtitle("subtitle")
            .dataLabelsEnabled(true)
            .backgroundColor("#FF000000")
            .axesTextColor("#FFFFFFFF")
            .scrollablePlotArea(
                scrollablePlotArea
            )
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

        aaChartView.aa_drawChartWithChartModel(aaChartModel)


//        val chartView = viewBinding.temperatureChart as VizContainerView
//
//        with(chartView) {
//
//            val temperatures = listOf(
//                5.2, 5.1, 6.1, 6.0, 5.9, 6.2,
//                6.4, 6.7, 7.8, 9.8, 12.3, 12.6,
//                12.6, 13.2, 14.6, 14.5, 14.7, 14.3,
//                13.8, 11.2, 9.4, 8.0, 6.9, 6.3
//            )
//
//            chart(temperatures) {
//                val tempDimension = quantitative( { domain } ) {
//                    name = "Temperature in °C"
//                }
//                val hourDimension = discrete( { indexInData } ) {
//                    name = "Time of the day"
//                }
//                line(hourDimension, tempDimension)
//            }
//        }

    }
}