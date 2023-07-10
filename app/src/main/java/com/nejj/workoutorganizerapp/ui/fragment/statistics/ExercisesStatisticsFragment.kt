package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.adapters.StatisticsOptionsAdapter
import com.nejj.workoutorganizerapp.enums.ExerciseStatisticsType
import com.nejj.workoutorganizerapp.enums.OverallStatisticsType
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.StatisticsDataSet
import com.nejj.workoutorganizerapp.ui.fragment.ItemsListViewFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedWorkoutRoutineViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.StatisticsViewModel
import kotlinx.coroutines.launch

class ExercisesStatisticsFragment : ItemsListViewFragment<StatisticsType>() {
    private val statisticsViewModel: StatisticsViewModel by activityViewModels()

    private val args: ExercisesStatisticsFragmentArgs by navArgs()

    companion object {
        val TAG = "ExercisesStatisticsFragment"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.fabAddItem.visibility = View.GONE

        val exercise = args.exercise
        requireActivity().title = exercise.name

        val sexerciseStatistics = ExerciseStatisticsType.values()
        simpleItemPreviewAdapter.differ.submitList(sexerciseStatistics.toList())
    }

    override val itemClickedListener = fun(statisticType: StatisticsType) {
        when(statisticType) {
            ExerciseStatisticsType.PERSONAL_RECORDS -> {
                val bundle = Bundle().apply {
                    putSerializable("exercise", args.exercise)
                }
                findNavController().navigate(
                    R.id.action_exercisesStatisticsFragment_to_personalRecordsStatisticsFragment,
                    bundle
                )
            }
            ExerciseStatisticsType.EST_ONE_REP_MAX -> {
                lifecycleScope.launch {
                    val oneRepMaxDataSet = statisticsViewModel.getEstimatedOneRepMaxForExercise(args.exercise.exerciseId!!)
                    navigateToStatisticsChart(oneRepMaxDataSet, statisticType)
                }
            }
            else -> {
                lifecycleScope.launch {
                    val dateDataSetMap = statisticsViewModel.getLoggedExerciseSetsWithDateMapByExercise(statisticType, args.exercise.exerciseId!!)

                    if(dateDataSetMap.isEmpty()) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("No data")
                            .setMessage("There is not enough data to show statistics.")
                            .setPositiveButton("OK") { dialog, which ->
                            }
                            .show()

                        Log.d(TAG, "dateVolumeMap is empty")
                        return@launch
                    }

                    navigateToStatisticsChart(dateDataSetMap, statisticType)
                }
            }
        }
    }

    private fun navigateToStatisticsChart(
        oneRepMaxDataSet: MutableMap<String, Any>,
        statisticType: StatisticsType
    ) {
        val (categories, dataSet) = oneRepMaxDataSet.toList().unzip()

        val statisticsDataSet = StatisticsDataSet(statisticType, categories, dataSet)

        val bundle = Bundle().apply {
            putSerializable("statisticsDataSet", statisticsDataSet)
        }

        findNavController().navigate(
            R.id.action_exercisesStatisticsFragment_to_statisticsChartFragment,
            bundle
        )
    }

    override val addItemListener = fun(_: View) {

    }

    override fun getAdapter(): SimpleItemPreviewAdapter<StatisticsType> {
        return StatisticsOptionsAdapter(true)
    }
}