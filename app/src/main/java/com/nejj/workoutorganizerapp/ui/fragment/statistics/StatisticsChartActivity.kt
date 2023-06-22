package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.nejj.workoutorganizerapp.databinding.FragmentStatisticChartBinding
import com.nejj.workoutorganizerapp.ui.charts.CanadaChart
import com.nejj.workoutorganizerapp.ui.charts.TemperatureChart
import io.data2viz.charts.chart.chart
import io.data2viz.charts.chart.discrete
import io.data2viz.charts.chart.mark.line
import io.data2viz.charts.chart.quantitative
import io.data2viz.viz.VizContainerView

class StatisticsChartActivity : AppCompatActivity() {

    private lateinit var viewBinding: FragmentStatisticChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(CanadaChart(this))

//        viewBinding = FragmentStatisticChartBinding.inflate(layoutInflater)
//        setContentView(viewBinding.root)
//
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