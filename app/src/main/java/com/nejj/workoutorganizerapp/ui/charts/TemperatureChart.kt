package com.nejj.workoutorganizerapp.ui.charts

import android.content.Context
import io.data2viz.charts.chart.Chart
import io.data2viz.charts.chart.chart
import io.data2viz.charts.chart.discrete
import io.data2viz.charts.chart.mark.line
import io.data2viz.charts.chart.quantitative
import io.data2viz.geom.Size
import io.data2viz.viz.VizContainerView


class TemperatureChart(context: Context) : VizContainerView(context) {

    private val temperatures = listOf(
        5.2, 5.1, 6.1, 6.0, 5.9, 6.2,
        6.4, 6.7, 7.8, 9.8, 12.3, 12.6,
        12.6, 13.2, 14.6, 14.5, 14.7, 14.3,
        13.8, 11.2, 9.4, 8.0, 6.9, 6.3
    )

    private val chart: Chart<Double> = chart(temperatures) {
        val tempDimension = quantitative( { domain } ) {
            name = "Temperature in °C"
        }
        val hourDimension = discrete( { indexInData } ) {
            name = "Time of the day"
        }
        line(hourDimension, tempDimension)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chart.size = Size(vizSize, vizSize * h / w)
    }
}